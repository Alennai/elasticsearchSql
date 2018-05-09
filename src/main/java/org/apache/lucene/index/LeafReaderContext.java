package org.apache.lucene.index;

import java.util.Collections;
import java.util.List;

/**
 * Created by xusiao on 2018/5/8.
 */
public final class LeafReaderContext  extends IndexReaderContext {
    /** The readers ord in the top-level's leaves array */
    public final int ord;
    /** The readers absolute doc base */
    public final int docBase;

    private final LeafReader reader;
    private final List<LeafReaderContext> leaves;

    /**
     * Creates a new {@link LeafReaderContext}
     */
    LeafReaderContext(CompositeReaderContext parent, LeafReader reader,
                      int ord, int docBase, int leafOrd, int leafDocBase) {
        super(parent, ord, docBase);
        this.ord = leafOrd;
        this.docBase = leafDocBase;
        this.reader = reader;
        this.leaves = isTopLevel ? Collections.singletonList(this) : null;
    }

    LeafReaderContext(LeafReader leafReader) {
        this(null, leafReader, 0, 0, 0, 0);
    }

    @Override
    public List<LeafReaderContext> leaves() {
        if (!isTopLevel) {
            throw new UnsupportedOperationException("This is not a top-level context.");
        }
        assert leaves != null;
        return leaves;
    }

    @Override
    public List<IndexReaderContext> children() {
        return null;
    }

    @Override
    public LeafReader reader() {
        return reader;
    }

    @Override
    public String toString() {
        return "LeafReaderContext(" + reader + " docBase=" + docBase + " ord=" + ord + ")";
    }
}