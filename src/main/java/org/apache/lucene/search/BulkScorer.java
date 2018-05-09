package org.apache.lucene.search;

import org.apache.lucene.util.Bits;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class BulkScorer {

    /** Scores and collects all matching documents.
     * @param collector The collector to which all matching documents are passed.
     * @param acceptDocs {@link Bits} that represents the allowed documents to match, or
     *                   {@code null} if they are all allowed to match.
     */
    public void score(LeafCollector collector, Bits acceptDocs) throws IOException {
        final int next = score(collector, acceptDocs, 0, DocIdSetIterator.NO_MORE_DOCS);
        assert next == DocIdSetIterator.NO_MORE_DOCS;
    }

    /**
     * Collects matching documents in a range and return an estimation of the
     * next matching document which is on or after {@code max}.
     * <p>The return value must be:</p><ul>
     *   <li>&gt;= {@code max},</li>
     *   <li>{@link DocIdSetIterator#NO_MORE_DOCS} if there are no more matches,</li>
     *   <li>&lt;= the first matching document that is &gt;= {@code max} otherwise.</li>
     * </ul>
     * <p>{@code min} is the minimum document to be considered for matching. All
     * documents strictly before this value must be ignored.</p>
     * <p>Although {@code max} would be a legal return value for this method, higher
     * values might help callers skip more efficiently over non-matching portions
     * of the docID space.</p>
     * <p>For instance, a {@link Scorer}-based implementation could look like
     * below:</p>
     * <pre class="prettyprint">
     * private final Scorer scorer; // set via constructor
     *
     * public int score(LeafCollector collector, Bits acceptDocs, int min, int max) throws IOException {
     *   collector.setScorer(scorer);
     *   int doc = scorer.docID();
     *   if (doc &lt; min) {
     *     doc = scorer.advance(min);
     *   }
     *   while (doc &lt; max) {
     *     if (acceptDocs == null || acceptDocs.get(doc)) {
     *       collector.collect(doc);
     *     }
     *     doc = scorer.nextDoc();
     *   }
     *   return doc;
     * }
     * </pre>
     *
     * @param  collector The collector to which all matching documents are passed.
     * @param acceptDocs {@link Bits} that represents the allowed documents to match, or
     *                   {@code null} if they are all allowed to match.
     * @param  min Score starting at, including, this document
     * @param  max Score up to, but not including, this doc
     * @return an under-estimation of the next matching doc after max
     */
    public abstract int score(LeafCollector collector, Bits acceptDocs, int min, int max) throws IOException;

    /**
     * Same as {@link DocIdSetIterator#cost()} for bulk scorers.
     */
    public abstract long cost();
}
