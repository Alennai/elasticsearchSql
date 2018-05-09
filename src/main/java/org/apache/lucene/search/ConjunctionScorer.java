package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by xusiao on 2018/5/8.
 */
 class ConjunctionScorer  extends Scorer {

    final DocIdSetIterator disi;
    final Scorer[] scorers;
    final float coord;

    /** Create a new {@link ConjunctionScorer}, note that {@code scorers} must be a subset of {@code required}. */
    ConjunctionScorer(Weight weight, Collection<Scorer> required, Collection<Scorer> scorers, float coord) {
        super(weight);
        assert required.containsAll(scorers);
        this.coord = coord;
        this.disi = ConjunctionDISI.intersectScorers(required);
        this.scorers = scorers.toArray(new Scorer[scorers.size()]);
    }

    @Override
    public TwoPhaseIterator twoPhaseIterator() {
        return TwoPhaseIterator.unwrap(disi);
    }

    @Override
    public DocIdSetIterator iterator() {
        return disi;
    }

    @Override
    public int docID() {
        return disi.docID();
    }

    @Override
    public float score() throws IOException {
        double sum = 0.0d;
        for (Scorer scorer : scorers) {
            sum += scorer.score();
        }
        return coord * (float)sum;
    }

    @Override
    public int freq() {
        return scorers.length;
    }

    @Override
    public Collection<ChildScorer> getChildren() {
        ArrayList<ChildScorer> children = new ArrayList<>();
        for (Scorer scorer : scorers) {
            children.add(new ChildScorer(scorer, "MUST"));
        }
        return children;
    }

    static final class DocsAndFreqs {
        final long cost;
        final DocIdSetIterator iterator;
        int doc = -1;

        DocsAndFreqs(DocIdSetIterator iterator) {
            this.iterator = iterator;
            this.cost = iterator.cost();
        }
    }
}
