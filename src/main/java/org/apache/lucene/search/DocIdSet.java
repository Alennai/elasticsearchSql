package org.apache.lucene.search;

import org.apache.lucene.util.Accountable;
import org.apache.lucene.util.Bits;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class DocIdSet  implements Accountable {

    /** An empty {@code DocIdSet} instance */
    public static final DocIdSet EMPTY = new DocIdSet() {

        @Override
        public DocIdSetIterator iterator() {
            return DocIdSetIterator.empty();
        }

        // we explicitly provide no random access, as this filter is 100% sparse and iterator exits faster
        @Override
        public Bits bits() {
            return null;
        }

        @Override
        public long ramBytesUsed() {
            return 0L;
        }
    };

    /** Provides a {@link DocIdSetIterator} to access the set.
     * This implementation can return <code>null</code> if there
     * are no docs that match. */
    public abstract DocIdSetIterator iterator() throws IOException;

    // TODO: somehow this class should express the cost of
    // iteration vs the cost of random access Bits; for
    // expensive Filters (e.g. distance < 1 km) we should use
    // bits() after all other Query/Filters have matched, but
    // this is the opposite of what bits() is for now
    // (down-low filtering using e.g. FixedBitSet)

    /** Optionally provides a {@link Bits} interface for random access
     * to matching documents.
     * @return {@code null}, if this {@code DocIdSet} does not support random access.
     * In contrast to {@link #iterator()}, a return value of {@code null}
     * <b>does not</b> imply that no documents match the filter!
     * The default implementation does not provide random access, so you
     * only need to implement this method if your DocIdSet can
     * guarantee random access to every docid in O(1) time without
     * external disk access (as {@link Bits} interface cannot throw
     * {@link IOException}). This is generally true for bit sets
     * like {@link org.apache.lucene.util.FixedBitSet}, which return
     * itself if they are used as {@code DocIdSet}.
     */
    public Bits bits() throws IOException {
        return null;
    }

}
