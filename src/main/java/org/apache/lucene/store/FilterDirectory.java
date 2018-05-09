package org.apache.lucene.store;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class FilterDirectory  extends Directory {

    /** Get the wrapped instance by <code>dir</code> as long as this reader is
     *  an instance of {@link FilterDirectory}.  */
    public static Directory unwrap(Directory dir) {
        while (dir instanceof FilterDirectory) {
            dir = ((FilterDirectory) dir).in;
        }
        return dir;
    }

    protected final Directory in;

    /** Sole constructor, typically called from sub-classes. */
    protected FilterDirectory(Directory in) {
        this.in = in;
    }

    /** Return the wrapped {@link Directory}. */
    public final Directory getDelegate() {
        return in;
    }

    @Override
    public String[] listAll() throws IOException {
        return in.listAll();
    }

    @Override
    public void deleteFile(String name) throws IOException {
        in.deleteFile(name);
    }

    @Override
    public long fileLength(String name) throws IOException {
        return in.fileLength(name);
    }

    @Override
    public IndexOutput createOutput(String name, IOContext context)
            throws IOException {
        return in.createOutput(name, context);
    }

    @Override
    public IndexOutput createTempOutput(String prefix, String suffix, IOContext context) throws IOException {
        return in.createTempOutput(prefix, suffix, context);
    }

    @Override
    public void sync(Collection<String> names) throws IOException {
        in.sync(names);
    }

    @Override
    public void rename(String source, String dest) throws IOException {
        in.rename(source, dest);
    }

    @Override
    public void syncMetaData() throws IOException {
        in.syncMetaData();
    }

    @Override
    public IndexInput openInput(String name, IOContext context)
            throws IOException {
        return in.openInput(name, context);
    }

    @Override
    public Lock obtainLock(String name) throws IOException {
        return in.obtainLock(name);
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + in.toString() + ")";
    }

}

