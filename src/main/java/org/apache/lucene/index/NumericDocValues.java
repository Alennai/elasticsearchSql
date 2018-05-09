package org.apache.lucene.index;

/**
 * Created by xusiao on 2018/5/8.
 */
public  abstract class NumericDocValues {

    /** Sole constructor. (For invocation by subclass
     *  constructors, typically implicit.) */
    protected NumericDocValues() {}

    /**
     * Returns the numeric value for the specified document ID.
     * @param docID document ID to lookup
     * @return numeric value
     */
    public abstract long get(int docID);
}
