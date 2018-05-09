package org.apache.lucene.util;

import java.util.Comparator;

/**
 * Created by xusiao on 2018/5/8.
 */
final class ArrayTimSorter<T>  extends TimSorter {

    private final Comparator<? super T> comparator;
    private final T[] arr;
    private final T[] tmp;

    /** Create a new {@link ArrayTimSorter}. */
    public ArrayTimSorter(T[] arr, Comparator<? super T> comparator, int maxTempSlots) {
        super(maxTempSlots);
        this.arr = arr;
        this.comparator = comparator;
        if (maxTempSlots > 0) {
            @SuppressWarnings("unchecked")
            final T[] tmp = (T[]) new Object[maxTempSlots];
            this.tmp = tmp;
        } else {
            this.tmp = null;
        }
    }

    @Override
    protected int compare(int i, int j) {
        return comparator.compare(arr[i], arr[j]);
    }

    @Override
    protected void swap(int i, int j) {
        ArrayUtil.swap(arr, i, j);
    }

    @Override
    protected void copy(int src, int dest) {
        arr[dest] = arr[src];
    }

    @Override
    protected void save(int start, int len) {
        System.arraycopy(arr, start, tmp, 0, len);
    }

    @Override
    protected void restore(int src, int dest) {
        arr[dest] = tmp[src];
    }

    @Override
    protected int compareSaved(int i, int j) {
        return comparator.compare(tmp[i], arr[j]);
    }

}

