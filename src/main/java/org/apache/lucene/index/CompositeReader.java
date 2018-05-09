package org.apache.lucene.index;

import java.util.List;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class CompositeReader extends IndexReader {

    private volatile CompositeReaderContext readerContext = null; // lazy init

    /** Sole constructor. (For invocation by subclass
     *  constructors, typically implicit.) */
    protected CompositeReader() {
        super();
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        // walk up through class hierarchy to get a non-empty simple name (anonymous classes have no name):
        for (Class<?> clazz = getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            if (!clazz.isAnonymousClass()) {
                buffer.append(clazz.getSimpleName());
                break;
            }
        }
        buffer.append('(');
        final List<? extends IndexReader> subReaders = getSequentialSubReaders();
        assert subReaders != null;
        if (!subReaders.isEmpty()) {
            buffer.append(subReaders.get(0));
            for (int i = 1, c = subReaders.size(); i < c; ++i) {
                buffer.append(" ").append(subReaders.get(i));
            }
        }
        buffer.append(')');
        return buffer.toString();
    }

    /** Expert: returns the sequential sub readers that this
     *  reader is logically composed of. This method may not
     *  return {@code null}.
     *
     *  <p><b>NOTE:</b> In contrast to previous Lucene versions this method
     *  is no longer public, code that wants to get all {@link LeafReader}s
     *  this composite is composed of should use {@link IndexReader#leaves()}.
     * @see IndexReader#leaves()
     */
    protected abstract List<? extends IndexReader> getSequentialSubReaders();

    @Override
    public final CompositeReaderContext getContext() {
        ensureOpen();
        // lazy init without thread safety for perf reasons: Building the readerContext twice does not hurt!
        if (readerContext == null) {
            assert getSequentialSubReaders() != null;
            readerContext = CompositeReaderContext.create(this);
        }
        return readerContext;
    }
}

