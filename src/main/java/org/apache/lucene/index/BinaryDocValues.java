package org.apache.lucene.index;

import org.apache.lucene.util.BytesRef;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class BinaryDocValues {

    /** Sole constructor. (For invocation by subclass
     * constructors, typically implicit.) */
    protected BinaryDocValues() {}

    /** Lookup the value for document.  The returned {@link BytesRef} may be
     * re-used across calls to {@link #get(int)} so make sure to
     * {@link BytesRef#deepCopyOf(BytesRef) copy it} if you want to keep it
     * around. */
    public abstract BytesRef get(int docID);
}

