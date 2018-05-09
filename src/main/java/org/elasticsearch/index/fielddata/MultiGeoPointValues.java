package org.elasticsearch.index.fielddata;

import org.elasticsearch.common.geo.GeoPoint;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class MultiGeoPointValues {

    /**
     * Creates a new {@link MultiGeoPointValues} instance
     */
    protected MultiGeoPointValues() {
    }

    /**
     * Sets iteration to the specified docID.
     * @param docId document ID
     *
     * @see #valueAt(int)
     * @see #count()
     */
    public abstract void setDocument(int docId);

    /**
     * Return the number of geo points the current document has.
     */
    public abstract int count();

    /**
     * Return the <code>i-th</code> value associated with the current document.
     * Behavior is undefined when <code>i</code> is undefined or greater than
     * or equal to {@link #count()}.
     *
     * Note: the returned {@link GeoPoint} might be shared across invocations.
     *
     * @return the next value for the current docID set to {@link #setDocument(int)}.
     */
    public abstract GeoPoint valueAt(int i);

}
