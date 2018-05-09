package org.apache.lucene.util;

import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.util.bkd.BKDWriter;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class PointValues {

    /** Maximum number of bytes for each dimension */
    public static final int MAX_NUM_BYTES = 16;

    /** Maximum number of dimensions */
    public static final int MAX_DIMENSIONS = BKDWriter.MAX_DIMS;

    /** Return the cumulated number of points across all leaves of the given
     * {@link IndexReader}. Leaves that do not have points for the given field
     * are ignored.
     *  @see PointValues#size(String) */
    public static long size(IndexReader reader, String field) throws IOException {
        long size = 0;
        for (LeafReaderContext ctx : reader.leaves()) {
            FieldInfo info = ctx.reader().getFieldInfos().fieldInfo(field);
            if (info == null || info.getPointDimensionCount() == 0) {
                continue;
            }
            PointValues values = ctx.reader().getPointValues();
            size += values.size(field);
        }
        return size;
    }

    /** Return the cumulated number of docs that have points across all leaves
     * of the given {@link IndexReader}. Leaves that do not have points for the
     * given field are ignored.
     *  @see PointValues#getDocCount(String) */
    public static int getDocCount(IndexReader reader, String field) throws IOException {
        int count = 0;
        for (LeafReaderContext ctx : reader.leaves()) {
            FieldInfo info = ctx.reader().getFieldInfos().fieldInfo(field);
            if (info == null || info.getPointDimensionCount() == 0) {
                continue;
            }
            PointValues values = ctx.reader().getPointValues();
            count += values.getDocCount(field);
        }
        return count;
    }

    /** Return the minimum packed values across all leaves of the given
     * {@link IndexReader}. Leaves that do not have points for the given field
     * are ignored.
     *  @see PointValues#getMinPackedValue(String) */
    public static byte[] getMinPackedValue(IndexReader reader, String field) throws IOException {
        byte[] minValue = null;
        for (LeafReaderContext ctx : reader.leaves()) {
            FieldInfo info = ctx.reader().getFieldInfos().fieldInfo(field);
            if (info == null || info.getPointDimensionCount() == 0) {
                continue;
            }
            PointValues values = ctx.reader().getPointValues();
            byte[] leafMinValue = values.getMinPackedValue(field);
            if (leafMinValue == null) {
                continue;
            }
            if (minValue == null) {
                minValue = leafMinValue.clone();
            } else {
                final int numDimensions = values.getNumDimensions(field);
                final int numBytesPerDimension = values.getBytesPerDimension(field);
                for (int i = 0; i < numDimensions; ++i) {
                    int offset = i * numBytesPerDimension;
                    if (StringHelper.compare(numBytesPerDimension, leafMinValue, offset, minValue, offset) < 0) {
                        System.arraycopy(leafMinValue, offset, minValue, offset, numBytesPerDimension);
                    }
                }
            }
        }
        return minValue;
    }

    /** Return the maximum packed values across all leaves of the given
     * {@link IndexReader}. Leaves that do not have points for the given field
     * are ignored.
     *  @see PointValues#getMaxPackedValue(String) */
    public static byte[] getMaxPackedValue(IndexReader reader, String field) throws IOException {
        byte[] maxValue = null;
        for (LeafReaderContext ctx : reader.leaves()) {
            FieldInfo info = ctx.reader().getFieldInfos().fieldInfo(field);
            if (info == null || info.getPointDimensionCount() == 0) {
                continue;
            }
            PointValues values = ctx.reader().getPointValues();
            byte[] leafMaxValue = values.getMaxPackedValue(field);
            if (leafMaxValue == null) {
                continue;
            }
            if (maxValue == null) {
                maxValue = leafMaxValue.clone();
            } else {
                final int numDimensions = values.getNumDimensions(field);
                final int numBytesPerDimension = values.getBytesPerDimension(field);
                for (int i = 0; i < numDimensions; ++i) {
                    int offset = i * numBytesPerDimension;
                    if (StringHelper.compare(numBytesPerDimension, leafMaxValue, offset, maxValue, offset) > 0) {
                        System.arraycopy(leafMaxValue, offset, maxValue, offset, numBytesPerDimension);
                    }
                }
            }
        }
        return maxValue;
    }

    /** Default constructor */
    protected PointValues() {
    }

    /** Used by {@link #intersect} to check how each recursive cell corresponds to the query. */
    public enum Relation {
        /** Return this if the cell is fully contained by the query */
        CELL_INSIDE_QUERY,
        /** Return this if the cell and query do not overlap */
        CELL_OUTSIDE_QUERY,
        /** Return this if the cell partially overlaps the query */
        CELL_CROSSES_QUERY
    };

    /** We recurse the BKD tree, using a provided instance of this to guide the recursion.
     *
     * @lucene.experimental */
    public interface IntersectVisitor {
        /** Called for all documents in a leaf cell that's fully contained by the query.  The
         *  consumer should blindly accept the docID. */
        void visit(int docID) throws IOException;

        /** Called for all documents in a leaf cell that crosses the query.  The consumer
         *  should scrutinize the packedValue to decide whether to accept it.  In the 1D case,
         *  values are visited in increasing order, and in the case of ties, in increasing
         *  docID order. */
        void visit(int docID, byte[] packedValue) throws IOException;

        /** Called for non-leaf cells to test how the cell relates to the query, to
         *  determine how to further recurse down the tree. */
        Relation compare(byte[] minPackedValue, byte[] maxPackedValue);

        /** Notifies the caller that this many documents are about to be visited */
        default void grow(int count) {};
    }

    /** Finds all documents and points matching the provided visitor.
     *  This method does not enforce live documents, so it's up to the caller
     *  to test whether each document is deleted, if necessary. */
    public abstract void intersect(String fieldName, IntersectVisitor visitor) throws IOException;

    /** Estimate the number of points that would be visited by {@link #intersect}
     * with the given {@link IntersectVisitor}. This should run many times faster
     * than {@link #intersect(String, IntersectVisitor)}.
     * @see DocIdSetIterator#cost */
    public abstract long estimatePointCount(String fieldName, IntersectVisitor visitor);

    /** Returns minimum value for each dimension, packed, or null if {@link #size} is <code>0</code> */
    public abstract byte[] getMinPackedValue(String fieldName) throws IOException;

    /** Returns maximum value for each dimension, packed, or null if {@link #size} is <code>0</code> */
    public abstract byte[] getMaxPackedValue(String fieldName) throws IOException;

    /** Returns how many dimensions were indexed */
    public abstract int getNumDimensions(String fieldName) throws IOException;

    /** Returns the number of bytes per dimension */
    public abstract int getBytesPerDimension(String fieldName) throws IOException;

    /** Returns the total number of indexed points across all documents in this field. */
    public abstract long size(String fieldName);

    /** Returns the total number of documents that have indexed at least one point for this field. */
    public abstract int getDocCount(String fieldName);
}

