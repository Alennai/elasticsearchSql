package org.apache.lucene.search;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class FieldComparatorSource  {

    /**
     * Creates a comparator for the field in the given index.
     *
     * @param fieldname
     *          Name of the field to create comparator for.
     * @return FieldComparator.
     */
    public abstract FieldComparator<?> newComparator(String fieldname, int numHits, int sortPos, boolean reversed);

}