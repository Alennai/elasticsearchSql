package org.apache.lucene.search;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public class DisjunctionDISIApproximation extends DocIdSetIterator {

    final DisiPriorityQueue subIterators;
    final long cost;

    public DisjunctionDISIApproximation(DisiPriorityQueue subIterators) {
        this.subIterators = subIterators;
        long cost = 0;
        for (DisiWrapper w : subIterators) {
            cost += w.cost;
        }
        this.cost = cost;
    }

    @Override
    public long cost() {
        return cost;
    }

    @Override
    public int docID() {
        return subIterators.top().doc;
    }

    @Override
    public int nextDoc() throws IOException {
        DisiWrapper top = subIterators.top();
        final int doc = top.doc;
        do {
            top.doc = top.approximation.nextDoc();
            top = subIterators.updateTop();
        } while (top.doc == doc);

        return top.doc;
    }

    @Override
    public int advance(int target) throws IOException {
        DisiWrapper top = subIterators.top();
        do {
            top.doc = top.approximation.advance(target);
            top = subIterators.updateTop();
        } while (top.doc < target);

        return top.doc;
    }
}

