package org.apache.lucene.search;

import org.apache.lucene.util.Bits;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by xusiao on 2018/5/8.
 */
 class BooleanTopLevelScorers  {

    /**
     * Used when there is more than one scorer in a query, but a segment
     * only had one non-null scorer. This just wraps that scorer directly
     * to factor in coord().
     */
    static class BoostedScorer extends FilterScorer {
        final float boost;

        BoostedScorer(Scorer in, float boost) {
            super(in);
            this.boost = boost;
        }

        @Override
        public float score() throws IOException {
            return in.score() * boost;
        }

        @Override
        public Collection<Scorer.ChildScorer> getChildren() {
            return Collections.singleton(new Scorer.ChildScorer(in, "BOOSTED"));
        }
    }

    /**
     * Used when there is more than one scorer in a query, but a segment
     * only had one non-null scorer.
     */
    static class BoostedBulkScorer extends BulkScorer {

        final BulkScorer in;
        final float boost;

        BoostedBulkScorer(BulkScorer scorer, float boost) {
            this.in = scorer;
            this.boost = boost;
        }

        @Override
        public int score(LeafCollector collector, Bits acceptDocs, int min, int max) throws IOException {
            final LeafCollector wrapped = new FilterLeafCollector(collector) {
                @Override
                public void setScorer(Scorer scorer) throws IOException {
                    super.setScorer(new BoostedScorer(scorer, boost));
                }
            };
            return in.score(wrapped, acceptDocs, min, max);
        }

        @Override
        public long cost() {
            return in.cost();
        }

    }

    /**
     * Used when there are both mandatory and optional clauses, but minShouldMatch
     * dictates that some of the optional clauses must match. The query is a conjunction,
     * but must compute coord based on how many optional subscorers matched (freq).
     */
    static class CoordinatingConjunctionScorer extends ConjunctionScorer {
        private final float coords[];
        private final int reqCount;
        private final Scorer req;
        private final Scorer opt;

        CoordinatingConjunctionScorer(Weight weight, float coords[], Scorer req, int reqCount, Scorer opt) {
            super(weight, Arrays.asList(req, opt), Arrays.asList(req, opt), 1f);
            this.coords = coords;
            this.req = req;
            this.reqCount = reqCount;
            this.opt = opt;
        }

        @Override
        public float score() throws IOException {
            return (req.score() + opt.score()) * coords[reqCount + opt.freq()];
        }
    }

    /**
     * Used when there are mandatory clauses with one optional clause: we compute
     * coord based on whether the optional clause matched or not.
     */
    static class ReqSingleOptScorer extends ReqOptSumScorer {
        // coord factor if just the required part matches
        private final float coordReq;
        // coord factor if both required and optional part matches
        private final float coordBoth;

        public ReqSingleOptScorer(Scorer reqScorer, Scorer optScorer, float coordReq, float coordBoth) {
            super(reqScorer, optScorer);
            this.coordReq = coordReq;
            this.coordBoth = coordBoth;
        }

        @Override
        public float score() throws IOException {
            // TODO: sum into a double and cast to float if we ever send required clauses to BS1
            int curDoc = reqScorer.docID();
            float score = reqScorer.score();

            int optScorerDoc = optIterator.docID();
            if (optScorerDoc < curDoc) {
                optScorerDoc = optIterator.advance(curDoc);
            }

            if (optScorerDoc == curDoc) {
                score = (score + optScorer.score()) * coordBoth;
            } else {
                score = score * coordReq;
            }

            return score;
        }
    }

    /**
     * Used when there are mandatory clauses with optional clauses: we compute
     * coord based on how many optional subscorers matched (freq).
     */
    static class ReqMultiOptScorer extends ReqOptSumScorer {
        private final int requiredCount;
        private final float coords[];

        public ReqMultiOptScorer(Scorer reqScorer, Scorer optScorer, int requiredCount, float coords[]) {
            super(reqScorer, optScorer);
            this.requiredCount = requiredCount;
            this.coords = coords;
        }

        @Override
        public float score() throws IOException {
            // TODO: sum into a double and cast to float if we ever send required clauses to BS1
            int curDoc = reqScorer.docID();
            float score = reqScorer.score();

            int optScorerDoc = optIterator.docID();
            if (optScorerDoc < curDoc) {
                optScorerDoc = optIterator.advance(curDoc);
            }

            if (optScorerDoc == curDoc) {
                score = (score + optScorer.score()) * coords[requiredCount + optScorer.freq()];
            } else {
                score = score * coords[requiredCount];
            }

            return score;
        }
    }
}

