package org.apache.lucene.search;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class FilterScorer  extends Scorer {
    protected final Scorer in;

    /**
     * Create a new FilterScorer
     * @param in the {@link Scorer} to wrap
     */
    public FilterScorer(Scorer in) {
        super(in.weight);
        this.in = in;
    }

    /**
     * Create a new FilterScorer with a specific weight
     * @param in the {@link Scorer} to wrap
     * @param weight a {@link Weight}
     */
    public FilterScorer(Scorer in, Weight weight) {
        super(weight);
        if (in == null) {
            throw new NullPointerException("wrapped Scorer must not be null");
        }
        this.in = in;
    }

    @Override
    public float score() throws IOException {
        return in.score();
    }

    @Override
    public int freq() throws IOException {
        return in.freq();
    }

    @Override
    public final int docID() {
        return in.docID();
    }

    @Override
    public final DocIdSetIterator iterator() {
        return in.iterator();
    }

    @Override
    public final TwoPhaseIterator twoPhaseIterator() {
        return in.twoPhaseIterator();
    }
}

