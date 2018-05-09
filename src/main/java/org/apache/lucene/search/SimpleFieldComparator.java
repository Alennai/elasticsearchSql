package org.apache.lucene.search;

import org.apache.lucene.index.LeafReaderContext;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class SimpleFieldComparator <T> extends FieldComparator<T> implements LeafFieldComparator {

    /** This method is called before collecting <code>context</code>. */
    protected abstract void doSetNextReader(LeafReaderContext context) throws IOException;

    @Override
    public final LeafFieldComparator getLeafComparator(LeafReaderContext context) throws IOException {
        doSetNextReader(context);
        return this;
    }

    @Override
    public void setScorer(Scorer scorer) throws IOException {}
}

