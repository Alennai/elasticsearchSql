package org.apache.lucene.search;

import java.util.Collection;

/**
 * Created by xusiao on 2018/5/8.
 */
final class FakeScorer extends Scorer {
    float score;
    int doc = -1;
    int freq = 1;

    public FakeScorer() {
        super(null);
    }

    @Override
    public int docID() {
        return doc;
    }

    @Override
    public int freq() {
        return freq;
    }

    @Override
    public float score() {
        return score;
    }

    @Override
    public DocIdSetIterator iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Weight getWeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<ChildScorer> getChildren() {
        throw new UnsupportedOperationException();
    }
}
