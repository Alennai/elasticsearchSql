package org.elasticsearch.index.fielddata;

import org.apache.lucene.util.BytesRef;

/**
 * Created by xusiao on 2018/5/7.
 */
public abstract class SortedBinaryDocValues {

    /**
     * Positions to the specified document
     */
    public abstract void setDocument(int docId);

    /**
     * Return the number of values of the current document.
     */
    public abstract int count();

    /**
     * Retrieve the value for the current document at the specified index.
     * An index ranges from {@code 0} to {@code count()-1}.
     * Note that the returned {@link BytesRef} might be reused across invocations.
     */
    public abstract BytesRef valueAt(int index);

}
