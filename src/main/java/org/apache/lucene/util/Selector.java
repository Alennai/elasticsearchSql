package org.apache.lucene.util;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class Selector {

    /** Reorder elements so that the element at position {@code k} is the same
     *  as if all elements were sorted and all other elements are partitioned
     *  around it: {@code [from, k)} only contains elements that are less than
     *  or equal to {@code k} and {@code (k, to)} only contains elements that
     *  are greater than or equal to {@code k}. */
    public abstract void select(int from, int to, int k);

    void checkArgs(int from, int to, int k) {
        if (k < from) {
            throw new IllegalArgumentException("k must be >= from");
        }
        if (k >= to) {
            throw new IllegalArgumentException("k must be < to");
        }
    }

    /** Swap values at slots <code>i</code> and <code>j</code>. */
    protected abstract void swap(int i, int j);
}

