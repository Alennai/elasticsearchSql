package org.apache.lucene.search;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface LeafCollector {

    /**
     * Called before successive calls to {@link #collect(int)}. Implementations
     * that need the score of the current document (passed-in to
     * {@link #collect(int)}), should save the passed-in Scorer and call
     * scorer.score() when needed.
     */
    void setScorer(Scorer scorer) throws IOException;

    /**
     * Called once for every document matching a query, with the unbased document
     * number.
     * <p>Note: The collection of the current segment can be terminated by throwing
     * a {@link }. In this case, the last docs of the
     * current {@link org.apache.lucene.index.LeafReaderContext} will be skipped and {@link IndexSearcher}
     * will swallow the exception and continue collection with the next leaf.
     * <p>
     * Note: This is called in an inner search loop. For good search performance,
     * implementations of this method should not call {@link IndexSearcher#doc(int)} or
     * {@link org.apache.lucene.index.IndexReader#document(int)} on every hit.
     * Doing so can slow searches by an order of magnitude or more.
     */
    void collect(int doc) throws IOException;

}

