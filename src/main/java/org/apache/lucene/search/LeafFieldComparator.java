package org.apache.lucene.search;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface LeafFieldComparator {

    /**
     * Set the bottom slot, ie the "weakest" (sorted last)
     * entry in the queue.  When {@link #compareBottom} is
     * called, you should compare against this slot.  This
     * will always be called before {@link #compareBottom}.
     *
     * @param slot the currently weakest (sorted last) slot in the queue
     */
    void setBottom(final int slot);

    /**
     * Compare the bottom of the queue with this doc.  This will
     * only invoked after setBottom has been called.  This
     * should return the same result as {@link
     * FieldComparator#compare(int,int)}} as if bottom were slot1 and the new
     * document were slot 2.
     *
     * <p>For a search that hits many results, this method
     * will be the hotspot (invoked by far the most
     * frequently).</p>
     *
     * @param doc that was hit
     * @return any {@code N < 0} if the doc's value is sorted after
     * the bottom entry (not competitive), any {@code N > 0} if the
     * doc's value is sorted before the bottom entry and {@code 0} if
     * they are equal.
     */
    int compareBottom(int doc) throws IOException;

    /**
     * Compare the top value with this doc.  This will
     * only invoked after setTopValue has been called.  This
     * should return the same result as {@link
     * FieldComparator#compare(int,int)}} as if topValue were slot1 and the new
     * document were slot 2.  This is only called for searches that
     * use searchAfter (deep paging).
     *
     * @param doc that was hit
     * @return any {@code N < 0} if the doc's value is sorted after
     * the top entry (not competitive), any {@code N > 0} if the
     * doc's value is sorted before the top entry and {@code 0} if
     * they are equal.
     */
    int compareTop(int doc) throws IOException;

    /**
     * This method is called when a new hit is competitive.
     * You should copy any state associated with this document
     * that will be required for future comparisons, into the
     * specified slot.
     *
     * @param slot which slot to copy the hit to
     * @param doc docID relative to current reader
     */
    void copy(int slot, int doc) throws IOException;

    /** Sets the Scorer to use in case a document's score is
     *  needed.
     *
     * @param scorer Scorer instance that you should use to
     * obtain the current hit's score, if necessary. */
    void setScorer(Scorer scorer) throws IOException;

}

