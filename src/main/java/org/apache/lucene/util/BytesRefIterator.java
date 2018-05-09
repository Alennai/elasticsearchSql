package org.apache.lucene.util;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface BytesRefIterator  {

    /**
     * Increments the iteration to the next {@link BytesRef} in the iterator.
     * Returns the resulting {@link BytesRef} or <code>null</code> if the end of
     * the iterator is reached. The returned BytesRef may be re-used across calls
     * to next. After this method returns null, do not call it again: the results
     * are undefined.
     *
     * @return the next {@link BytesRef} in the iterator or <code>null</code> if
     *         the end of the iterator is reached.
     * @throws IOException If there is a low-level I/O error.
     */
    public BytesRef next() throws IOException;

    /** Singleton BytesRefIterator that iterates over 0 BytesRefs. */
    public static final BytesRefIterator EMPTY = new BytesRefIterator() {

        @Override
        public BytesRef next() {
            return null;
        }
    };
}