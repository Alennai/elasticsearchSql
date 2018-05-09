package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Set;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class ConstantScoreWeight  extends Weight {

    private float boost;
    private float queryNorm;
    private float queryWeight;

    protected ConstantScoreWeight(Query query) {
        super(query);
        normalize(1f, 1f);
    }

    @Override
    public void extractTerms(Set<Term> terms) {
        // most constant-score queries don't wrap index terms
        // eg. geo filters, doc values queries, ...
        // override if your constant-score query does wrap terms
    }

    @Override
    public final float getValueForNormalization() throws IOException {
        return queryWeight * queryWeight;
    }

    @Override
    public void normalize(float norm, float boost) {
        this.boost = boost;
        queryNorm = norm;
        queryWeight = queryNorm * boost;
    }

    /** Return the normalization factor for this weight. */
    protected final float queryNorm() {
        return queryNorm;
    }

    /** Return the boost for this weight. */
    protected final float boost() {
        return boost;
    }

    /** Return the score produced by this {@link Weight}. */
    protected final float score() {
        return queryWeight;
    }

    @Override
    public Explanation explain(LeafReaderContext context, int doc) throws IOException {
        final Scorer s = scorer(context);
        final boolean exists;
        if (s == null) {
            exists = false;
        } else {
            final TwoPhaseIterator twoPhase = s.twoPhaseIterator();
            if (twoPhase == null) {
                exists = s.iterator().advance(doc) == doc;
            } else {
                exists = twoPhase.approximation().advance(doc) == doc && twoPhase.matches();
            }
        }

        if (exists) {
            return Explanation.match(
                    queryWeight, getQuery().toString() + ", product of:",
                    Explanation.match(boost, "boost"), Explanation.match(queryNorm, "queryNorm"));
        } else {
            return Explanation.noMatch(getQuery().toString() + " doesn't match id " + doc);
        }
    }

}
