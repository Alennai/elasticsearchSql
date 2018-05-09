package org.apache.lucene.search;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class Scorer  {
    /** the Scorer's parent Weight. in some cases this may be null */
    // TODO can we clean this up?
    protected final Weight weight;

    /**
     * Constructs a Scorer
     * @param weight The scorers <code>Weight</code>.
     */
    protected Scorer(Weight weight) {
        this.weight = weight;
    }

    /**
     * Returns the doc ID that is currently being scored.
     * This will return {@code -1} if the {@link #iterator()} is not positioned
     * or {@link DocIdSetIterator#NO_MORE_DOCS} if it has been entirely consumed.
     * @see DocIdSetIterator#docID()
     */
    public abstract int docID();

    /** Returns the score of the current document matching the query.
     * Initially invalid, until {@link DocIdSetIterator#nextDoc()} or
     * {@link DocIdSetIterator#advance(int)} is called on the {@link #iterator()}
     * the first time, or when called from within {@link LeafCollector#collect}.
     */
    public abstract float score() throws IOException;

    /** Returns the freq of this Scorer on the current document */
    public abstract int freq() throws IOException;

    /** returns parent Weight
     * @lucene.experimental
     */
    public Weight getWeight() {
        return weight;
    }

    /**
     * Returns child sub-scorers positioned on the current document
     *
     * Note that this method should not be called on Scorers passed to {@link LeafCollector#setScorer(Scorer)},
     * as these may be synthetic Scorers produced by {@link BulkScorer} which will throw an Exception.
     *
     * @lucene.experimental
     */
    public Collection<ChildScorer> getChildren() throws IOException {
        return Collections.emptyList();
    }

    /** A child Scorer and its relationship to its parent.
     * the meaning of the relationship depends upon the parent query.
     * @lucene.experimental */
    public static class ChildScorer {
        /**
         * Child Scorer. (note this is typically a direct child, and may
         * itself also have children).
         */
        public final Scorer child;
        /**
         * An arbitrary string relating this scorer to the parent.
         */
        public final String relationship;

        /**
         * Creates a new ChildScorer node with the specified relationship.
         * <p>
         * The relationship can be any be any string that makes sense to
         * the parent Scorer.
         */
        public ChildScorer(Scorer child, String relationship) {
            this.child = child;
            this.relationship = relationship;
        }
    }

    /**
     * Return a {@link DocIdSetIterator} over matching documents.
     *
     * The returned iterator will either be positioned on {@code -1} if no
     * documents have been scored yet, {@link DocIdSetIterator#NO_MORE_DOCS}
     * if all documents have been scored already, or the last document id that
     * has been scored otherwise.
     *
     * The returned iterator is a view: calling this method several times will
     * return iterators that have the same state.
     */
    public abstract DocIdSetIterator iterator();

    /**
     * Optional method: Return a {@link TwoPhaseIterator} view of this
     * {@link Scorer}. A return value of {@code null} indicates that
     * two-phase iteration is not supported.
     *
     * Note that the returned {@link TwoPhaseIterator}'s
     * {@link TwoPhaseIterator#approximation() approximation} must
     * advance synchronously with the {@link #iterator()}: advancing the
     * approximation must advance the iterator and vice-versa.
     *
     * Implementing this method is typically useful on {@link Scorer}s
     * that have a high per-document overhead in order to confirm matches.
     *
     * The default implementation returns {@code null}.
     */
    public TwoPhaseIterator twoPhaseIterator() {
        return null;
    }
}
