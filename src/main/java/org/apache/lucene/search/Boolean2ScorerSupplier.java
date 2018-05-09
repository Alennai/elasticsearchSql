package org.apache.lucene.search;

import org.apache.lucene.util.*;
import org.apache.lucene.util.PriorityQueue;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by xusiao on 2018/5/8.
 */
final class Boolean2ScorerSupplier  extends ScorerSupplier {

    private final BooleanWeight weight;
    private final Map<BooleanClause.Occur, Collection<ScorerSupplier>> subs;
    private final boolean needsScores;
    private final boolean disableCoord;
    private final float[] coords;
    private final int maxCoord;
    private final int minShouldMatch;
    private long cost = -1;

    Boolean2ScorerSupplier(BooleanWeight weight,
                           Map<BooleanClause.Occur, Collection<ScorerSupplier>> subs,
                           boolean disableCoord, float[] coords, int maxCoord,
                           boolean needsScores, int minShouldMatch) {
        if (minShouldMatch < 0) {
            throw new IllegalArgumentException("minShouldMatch must be positive, but got: " + minShouldMatch);
        }
        if (minShouldMatch != 0 && minShouldMatch >= subs.get(BooleanClause.Occur.SHOULD).size()) {
            throw new IllegalArgumentException("minShouldMatch must be strictly less than the number of SHOULD clauses");
        }
        if (needsScores == false && minShouldMatch == 0 && subs.get(BooleanClause.Occur.SHOULD).size() > 0
                && subs.get(BooleanClause.Occur.MUST).size() + subs.get(BooleanClause.Occur.FILTER).size() > 0) {
            throw new IllegalArgumentException("Cannot pass purely optional clauses if scores are not needed");
        }
        if (subs.get(BooleanClause.Occur.SHOULD).size() + subs.get(BooleanClause.Occur.MUST).size() + subs.get(BooleanClause.Occur.FILTER).size() == 0) {
            throw new IllegalArgumentException("There should be at least one positive clause");
        }
        this.weight = weight;
        this.subs = subs;
        this.disableCoord = disableCoord;
        this.coords = coords;
        this.maxCoord = maxCoord;
        this.needsScores = needsScores;
        this.minShouldMatch = minShouldMatch;
    }

    private long computeCost() {
        OptionalLong minRequiredCost = Stream.concat(
                subs.get(BooleanClause.Occur.MUST).stream(),
                subs.get(BooleanClause.Occur.FILTER).stream())
                .mapToLong(ScorerSupplier::cost)
                .min();
        if (minRequiredCost.isPresent() && minShouldMatch == 0) {
            return minRequiredCost.getAsLong();
        } else {
            final Collection<ScorerSupplier> optionalScorers = subs.get(BooleanClause.Occur.SHOULD);
            final long shouldCost = MinShouldMatchSumScorer.cost(
                    optionalScorers.stream().mapToLong(ScorerSupplier::cost),
                    optionalScorers.size(), minShouldMatch);
            return Math.min(minRequiredCost.orElse(Long.MAX_VALUE), shouldCost);
        }
    }

    @Override
    public long cost() {
        if (cost == -1) {
            cost = computeCost();
        }
        return cost;
    }

    @Override
    public Scorer get(boolean randomAccess) throws IOException {
        // three cases: conjunction, disjunction, or mix

        // pure conjunction
        if (subs.get(BooleanClause.Occur.SHOULD).isEmpty()) {
            return excl(req(subs.get(BooleanClause.Occur.FILTER), subs.get(BooleanClause.Occur.MUST), disableCoord, randomAccess), subs.get(BooleanClause.Occur.MUST_NOT));
        }

        // pure disjunction
        if (subs.get(BooleanClause.Occur.FILTER).isEmpty() && subs.get(BooleanClause.Occur.MUST).isEmpty()) {
            return excl(opt(subs.get(BooleanClause.Occur.SHOULD), minShouldMatch, needsScores, disableCoord, randomAccess), subs.get(BooleanClause.Occur.MUST_NOT));
        }

        // conjunction-disjunction mix:
        // we create the required and optional pieces, and then
        // combine the two: if minNrShouldMatch > 0, then it's a conjunction: because the
        // optional side must match. otherwise it's required + optional

        boolean reqRandomAccess = randomAccess;
        boolean shouldRandomAccess = randomAccess || minShouldMatch == 0;
        if (randomAccess == false && minShouldMatch > 0) {
            // We need to figure out whether the MUST/FILTER or the SHOULD clauses would lead the iteration
            final long reqCost = Stream.concat(
                    subs.get(BooleanClause.Occur.MUST).stream(),
                    subs.get(BooleanClause.Occur.FILTER).stream())
                    .mapToLong(ScorerSupplier::cost)
                    .min().getAsLong();
            final long msmCost = MinShouldMatchSumScorer.cost(
                    subs.get(BooleanClause.Occur.SHOULD).stream().mapToLong(ScorerSupplier::cost),
                    subs.get(BooleanClause.Occur.SHOULD).size(), minShouldMatch);
            reqRandomAccess = reqCost > msmCost;
            shouldRandomAccess = msmCost > reqCost;
        }

        Scorer req = excl(req(subs.get(BooleanClause.Occur.FILTER), subs.get(BooleanClause.Occur.MUST), true, reqRandomAccess), subs.get(BooleanClause.Occur.MUST_NOT));
        Scorer opt = opt(subs.get(BooleanClause.Occur.SHOULD), minShouldMatch, needsScores, true, shouldRandomAccess);


        // TODO: clean this up: it's horrible
        if (disableCoord) {
            if (minShouldMatch > 0) {
                return new ConjunctionScorer(weight, Arrays.asList(req, opt), Arrays.asList(req, opt), 1F);
            } else {
                return new ReqOptSumScorer(req, opt);
            }
        } else if (subs.get(BooleanClause.Occur.SHOULD).size() == 1) {
            if (minShouldMatch > 0) {
                return new ConjunctionScorer(weight, Arrays.asList(req, opt), Arrays.asList(req, opt), weight.coord(subs.get(BooleanClause.Occur.MUST).size()+1, maxCoord));
            } else {
                float coordReq = weight.coord(subs.get(BooleanClause.Occur.MUST).size(), maxCoord);
                float coordBoth = weight.coord(subs.get(BooleanClause.Occur.MUST).size() + 1, maxCoord);
                return new BooleanTopLevelScorers.ReqSingleOptScorer(req, opt, coordReq, coordBoth);
            }
        } else {
            if (minShouldMatch > 0) {
                return new BooleanTopLevelScorers.CoordinatingConjunctionScorer(weight, coords, req, subs.get(BooleanClause.Occur.MUST).size(), opt);
            } else {
                return new BooleanTopLevelScorers.ReqMultiOptScorer(req, opt, subs.get(BooleanClause.Occur.MUST).size(), coords);
            }
        }
    }

    /** Create a new scorer for the given required clauses. Note that
     *  {@code requiredScoring} is a subset of {@code required} containing
     *  required clauses that should participate in scoring. */
    private Scorer req(Collection<ScorerSupplier> requiredNoScoring, Collection<ScorerSupplier> requiredScoring,
                       boolean disableCoord, boolean randomAccess) throws IOException {
        if (requiredNoScoring.size() + requiredScoring.size() == 1) {
            Scorer req = (requiredNoScoring.isEmpty() ? requiredScoring : requiredNoScoring).iterator().next().get(randomAccess);

            if (needsScores == false) {
                return req;
            }

            if (requiredScoring.isEmpty()) {
                // Scores are needed but we only have a filter clause
                // BooleanWeight expects that calling score() is ok so we need to wrap
                // to prevent score() from being propagated
                return new FilterScorer(req) {
                    @Override
                    public float score() throws IOException {
                        return 0f;
                    }
                    @Override
                    public int freq() throws IOException {
                        return 0;
                    }
                };
            }

            float boost = 1f;
            if (disableCoord == false) {
                boost = weight.coord(1, maxCoord);
            }
            if (boost == 1f) {
                return req;
            }
            return new BooleanTopLevelScorers.BoostedScorer(req, boost);
        } else {
            long minCost = Math.min(
                    requiredNoScoring.stream().mapToLong(ScorerSupplier::cost).min().orElse(Long.MAX_VALUE),
                    requiredScoring.stream().mapToLong(ScorerSupplier::cost).min().orElse(Long.MAX_VALUE));
            List<Scorer> requiredScorers = new ArrayList<>();
            List<Scorer> scoringScorers = new ArrayList<>();
            for (ScorerSupplier s : requiredNoScoring) {
                requiredScorers.add(s.get(randomAccess || s.cost() > minCost));
            }
            for (ScorerSupplier s : requiredScoring) {
                Scorer scorer = s.get(randomAccess || s.cost() > minCost);
                requiredScorers.add(scorer);
                scoringScorers.add(scorer);
            }
            return new ConjunctionScorer(weight, requiredScorers, scoringScorers,
                    disableCoord || needsScores == false ? 1.0F : weight.coord(requiredScoring.size(), maxCoord));
        }
    }

    private Scorer excl(Scorer main, Collection<ScorerSupplier> prohibited) throws IOException {
        if (prohibited.isEmpty()) {
            return main;
        } else {
            return new ReqExclScorer(main, opt(prohibited, 1, false, true, true));
        }
    }

    private Scorer opt(Collection<ScorerSupplier> optional, int minShouldMatch,
                       boolean needsScores, boolean disableCoord, boolean randomAccess) throws IOException {
        if (optional.size() == 1) {
            Scorer opt = optional.iterator().next().get(randomAccess);
            if (needsScores && !disableCoord && maxCoord > 1) {
                return new BooleanTopLevelScorers.BoostedScorer(opt, weight.coord(1, maxCoord));
            } else {
                return opt;
            }
        } else {
            float coords[];
            if (disableCoord || needsScores == false) {
                // sneaky: when we do a mixed conjunction/disjunction, we need a fake for the disjunction part.
                coords = new float[optional.size()+1];
                Arrays.fill(coords, 1F);
            } else {
                coords = this.coords;
            }
            if (minShouldMatch > 1) {
                final List<Scorer> optionalScorers = new ArrayList<>();
                final org.apache.lucene.util.PriorityQueue<ScorerSupplier> pq = new org.apache.lucene.util.PriorityQueue<ScorerSupplier>(subs.get(BooleanClause.Occur.SHOULD).size() - minShouldMatch + 1) {
                    @Override
                    protected boolean lessThan(ScorerSupplier a, ScorerSupplier b) {
                        return a.cost() > b.cost();
                    }
                };
                for (ScorerSupplier scorer : subs.get(BooleanClause.Occur.SHOULD)) {
                    ScorerSupplier overflow = pq.insertWithOverflow(scorer);
                    if (overflow != null) {
                        optionalScorers.add(overflow.get(true));
                    }
                }
                for (ScorerSupplier scorer : pq) {
                    optionalScorers.add(scorer.get(randomAccess));
                }
                return new MinShouldMatchSumScorer(weight, optionalScorers, minShouldMatch, coords);
            } else {
                final List<Scorer> optionalScorers = new ArrayList<>();
                for (ScorerSupplier scorer : optional) {
                    optionalScorers.add(scorer.get(randomAccess));
                }
                return new DisjunctionSumScorer(weight, optionalScorers, coords, needsScores);
            }
        }
    }

}
