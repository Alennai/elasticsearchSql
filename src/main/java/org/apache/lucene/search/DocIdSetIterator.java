package org.apache.lucene.search;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class DocIdSetIterator {

    /** An empty {@code DocIdSetIterator} instance */
    public static final DocIdSetIterator empty() {
        return new DocIdSetIterator() {
            boolean exhausted = false;

            @Override
            public int advance(int target) {
                assert !exhausted;
                assert target >= 0;
                exhausted = true;
                return NO_MORE_DOCS;
            }

            @Override
            public int docID() {
                return exhausted ? NO_MORE_DOCS : -1;
            }
            @Override
            public int nextDoc() {
                assert !exhausted;
                exhausted = true;
                return NO_MORE_DOCS;
            }

            @Override
            public long cost() {
                return 0;
            }
        };
    }

    /** A {@link DocIdSetIterator} that matches all documents up to
     *  {@code maxDoc - 1}. */
    public static final DocIdSetIterator all(int maxDoc) {
        return new DocIdSetIterator() {
            int doc = -1;

            @Override
            public int docID() {
                return doc;
            }

            @Override
            public int nextDoc() throws IOException {
                return advance(doc + 1);
            }

            @Override
            public int advance(int target) throws IOException {
                doc = target;
                if (doc >= maxDoc) {
                    doc = NO_MORE_DOCS;
                }
                return doc;
            }

            @Override
            public long cost() {
                return maxDoc;
            }
        };
    }

    /**
     * When returned by {@link #nextDoc()}, {@link #advance(int)} and
     * {@link #docID()} it means there are no more docs in the iterator.
     */
    public static final int NO_MORE_DOCS = Integer.MAX_VALUE;

    /**
     * Returns the following:
     * <ul>
     * <li><code>-1</code> if {@link #nextDoc()} or
     * {@link #advance(int)} were not called yet.
     * <li>{@link #NO_MORE_DOCS} if the iterator has exhausted.
     * <li>Otherwise it should return the doc ID it is currently on.
     * </ul>
     * <p>
     *
     * @since 2.9
     */
    public abstract int docID();

    /**
     * Advances to the next document in the set and returns the doc it is
     * currently on, or {@link #NO_MORE_DOCS} if there are no more docs in the
     * set.<br>
     *
     * <b>NOTE:</b> after the iterator has exhausted you should not call this
     * method, as it may result in unpredicted behavior.
     *
     * @since 2.9
     */
    public abstract int nextDoc() throws IOException;

    /**
     * Advances to the first beyond the current whose document number is greater
     * than or equal to <i>target</i>, and returns the document number itself.
     * Exhausts the iterator and returns {@link #NO_MORE_DOCS} if <i>target</i>
     * is greater than the highest document number in the set.
     * <p>
     * The behavior of this method is <b>undefined</b> when called with
     * <code> target &le; current</code>, or after the iterator has exhausted.
     * Both cases may result in unpredicted behavior.
     * <p>
     * When <code> target &gt; current</code> it behaves as if written:
     *
     * <pre class="prettyprint">
     * int advance(int target) {
     *   int doc;
     *   while ((doc = nextDoc()) &lt; target) {
     *   }
     *   return doc;
     * }
     * </pre>
     *
     * Some implementations are considerably more efficient than that.
     * <p>
     * <b>NOTE:</b> this method may be called with {@link #NO_MORE_DOCS} for
     * efficiency by some Scorers. If your implementation cannot efficiently
     * determine that it should exhaust, it is recommended that you check for that
     * value in each call to this method.
     * <p>
     *
     * @since 2.9
     */
    public abstract int advance(int target) throws IOException;

    /** Slow (linear) implementation of {@link #advance} relying on
     *  {@link #nextDoc()} to advance beyond the target position. */
    protected final int slowAdvance(int target) throws IOException {
        assert docID() < target;
        int doc;
        do {
            doc = nextDoc();
        } while (doc < target);
        return doc;
    }

    /**
     * Returns the estimated cost of this {@link DocIdSetIterator}.
     * <p>
     * This is generally an upper bound of the number of documents this iterator
     * might match, but may be a rough heuristic, hardcoded value, or otherwise
     * completely inaccurate.
     */
    public abstract long cost();

}
