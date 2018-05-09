package org.apache.lucene.index;

import java.util.List;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class IndexReaderContext {
    /** The reader context for this reader's immediate parent, or null if none */
    public final CompositeReaderContext parent;
    /** <code>true</code> if this context struct represents the top level reader within the hierarchical context */
    public final boolean isTopLevel;
    /** the doc base for this reader in the parent, <tt>0</tt> if parent is null */
    public final int docBaseInParent;
    /** the ord for this reader in the parent, <tt>0</tt> if parent is null */
    public final int ordInParent;

    // An object that uniquely identifies this context without referencing
    // segments. The goal is to make it fine to have references to this
    // identity object, even after the index reader has been closed
    final Object identity = new Object();

    IndexReaderContext(CompositeReaderContext parent, int ordInParent, int docBaseInParent) {
        if (!(this instanceof CompositeReaderContext || this instanceof LeafReaderContext))
            throw new Error("This class should never be extended by custom code!");
        this.parent = parent;
        this.docBaseInParent = docBaseInParent;
        this.ordInParent = ordInParent;
        this.isTopLevel = parent==null;
    }

    /** Expert: Return an {@link Object} that uniquely identifies this context.
     *  The returned object does neither reference this {@link IndexReaderContext}
     *  nor the wrapped {@link IndexReader}.
     *  @lucene.experimental */
    public Object id() {
        return identity;
    }

    /** Returns the {@link IndexReader}, this context represents. */
    public abstract IndexReader reader();

    /**
     * Returns the context's leaves if this context is a top-level context.
     * For convenience, if this is an {@link LeafReaderContext} this
     * returns itself as the only leaf.
     * <p>Note: this is convenience method since leaves can always be obtained by
     * walking the context tree using {@link #children()}.
     * @throws UnsupportedOperationException if this is not a top-level context.
     * @see #children()
     */
    public abstract List<LeafReaderContext> leaves() throws UnsupportedOperationException;

    /**
     * Returns the context's children iff this context is a composite context
     * otherwise <code>null</code>.
     */
    public abstract List<IndexReaderContext> children();
}
