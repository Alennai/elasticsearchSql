package org.apache.lucene.search.spans;

import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TwoPhaseIterator;
import org.apache.lucene.search.similarities.Similarity;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class Spans  extends DocIdSetIterator {

    public static final int NO_MORE_POSITIONS = Integer.MAX_VALUE;

    /**
     * Returns the next start position for the current doc.
     * There is always at least one start/end position per doc.
     * After the last start/end position at the current doc this returns {@link #NO_MORE_POSITIONS}.
     */
    public abstract int nextStartPosition() throws IOException;

    /**
     * Returns the start position in the current doc, or -1 when {@link #nextStartPosition} was not yet called on the current doc.
     * After the last start/end position at the current doc this returns {@link #NO_MORE_POSITIONS}.
     */
    public abstract int startPosition();

    /**
     * Returns the end position for the current start position, or -1 when {@link #nextStartPosition} was not yet called on the current doc.
     * After the last start/end position at the current doc this returns {@link #NO_MORE_POSITIONS}.
     */
    public abstract int endPosition();

    /**
     * Return the width of the match, which is typically used to compute
     * the {@link Similarity.SimScorer#computeSlopFactor(int) slop factor}. It is only legal
     * to call this method when the iterator is on a valid doc ID and positioned.
     * The return value must be positive, and lower values means that the match is
     * better.
     */
    public abstract int width();

    /**
     * Collect postings data from the leaves of the current Spans.
     *
     * This method should only be called after {@link #nextStartPosition()}, and before
     * {@link #NO_MORE_POSITIONS} has been reached.
     *
     * @param collector a SpanCollector
     *
     * @lucene.experimental
     */
    public abstract void collect(SpanCollector collector) throws IOException;

    /**
     * Return an estimation of the cost of using the positions of
     * this {@link Spans} for any single document, but only after
     * {@link #asTwoPhaseIterator} returned {@code null}.
     * Otherwise this method should not be called.
     * The returned value is independent of the current document.
     *
     * @lucene.experimental
     */
    public abstract float positionsCost();

    /**
     * Optional method: Return a {@link TwoPhaseIterator} view of this
     * {@link Scorer}. A return value of {@code null} indicates that
     * two-phase iteration is not supported.
     * @see Scorer#twoPhaseIterator()
     */
    public TwoPhaseIterator asTwoPhaseIterator() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Class<? extends Spans> clazz = getClass();
        sb.append(clazz.isAnonymousClass() ? clazz.getName() : clazz.getSimpleName());
        sb.append("(doc=").append(docID());
        sb.append(",start=").append(startPosition());
        sb.append(",end=").append(endPosition());
        sb.append(")");
        return sb.toString();
    }

    /**
     * Called before the current doc's frequency is calculated
     */
    protected void doStartCurrentDoc() throws IOException {}

    /**
     * Called each time the scorer's SpanScorer is advanced during frequency calculation
     */
    protected void doCurrentSpans() throws IOException {}

}
