package org.apache.lucene.index;

/**
 * Created by xusiao on 2018/5/7.
 */
public abstract class SortedNumericDoubleValues {

    /** Sole constructor. (For invocation by subclass
     * constructors, typically implicit.) */
    protected SortedNumericDoubleValues() {}

    /**
     * Positions to the specified document
     */
    public abstract void setDocument(int doc);

    /**
     * Retrieve the value for the current document at the specified index.
     * An index ranges from {@code 0} to {@code count()-1}.
     */
    public abstract double valueAt(int index);

    /**
     * Retrieves the count of values for the current document.
     * This may be zero if a document has no values.
     */
    public abstract int count();

}

