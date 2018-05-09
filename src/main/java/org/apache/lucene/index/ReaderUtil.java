package org.apache.lucene.index;

import java.util.List;

/**
 * Created by xusiao on 2018/5/8.
 */
public final class ReaderUtil {

    private ReaderUtil() {} // no instance

    /**
     * Walks up the reader tree and return the given context's top level reader
     * context, or in other words the reader tree's root context.
     */
    public static IndexReaderContext getTopLevelContext(IndexReaderContext context) {
        while (context.parent != null) {
            context = context.parent;
        }
        return context;
    }

    /**
     * Returns index of the searcher/reader for document <code>n</code> in the
     * array used to construct this searcher/reader.
     */
    public static int subIndex(int n, int[] docStarts) { // find
        // searcher/reader for doc n:
        int size = docStarts.length;
        int lo = 0; // search starts array
        int hi = size - 1; // for first element less than n, return its index
        while (hi >= lo) {
            int mid = (lo + hi) >>> 1;
            int midValue = docStarts[mid];
            if (n < midValue)
                hi = mid - 1;
            else if (n > midValue)
                lo = mid + 1;
            else { // found a match
                while (mid + 1 < size && docStarts[mid + 1] == midValue) {
                    mid++; // scan to last match
                }
                return mid;
            }
        }
        return hi;
    }

    /**
     * Returns index of the searcher/reader for document <code>n</code> in the
     * array used to construct this searcher/reader.
     */
    public static int subIndex(int n, List<LeafReaderContext> leaves) { // find
        // searcher/reader for doc n:
        int size = leaves.size();
        int lo = 0; // search starts array
        int hi = size - 1; // for first element less than n, return its index
        while (hi >= lo) {
            int mid = (lo + hi) >>> 1;
            int midValue = leaves.get(mid).docBase;
            if (n < midValue)
                hi = mid - 1;
            else if (n > midValue)
                lo = mid + 1;
            else { // found a match
                while (mid + 1 < size && leaves.get(mid + 1).docBase == midValue) {
                    mid++; // scan to last match
                }
                return mid;
            }
        }
        return hi;
    }
}
