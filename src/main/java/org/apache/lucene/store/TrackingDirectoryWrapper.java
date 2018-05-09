package org.apache.lucene.store;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xusiao on 2018/5/8.
 */
public final class TrackingDirectoryWrapper  extends FilterDirectory {

    private final Set<String> createdFileNames = Collections.synchronizedSet(new HashSet<String>());

    public TrackingDirectoryWrapper(Directory in) {
        super(in);
    }

    @Override
    public void deleteFile(String name) throws IOException {
        in.deleteFile(name);
        createdFileNames.remove(name);
    }

    @Override
    public IndexOutput createOutput(String name, IOContext context) throws IOException {
        IndexOutput output = in.createOutput(name, context);
        createdFileNames.add(name);
        return output;
    }

    @Override
    public IndexOutput createTempOutput(String prefix, String suffix, IOContext context)
            throws IOException {
        IndexOutput tempOutput = in.createTempOutput(prefix, suffix, context);
        createdFileNames.add(tempOutput.getName());
        return tempOutput;
    }

    @Override
    public void copyFrom(Directory from, String src, String dest, IOContext context) throws IOException {
        in.copyFrom(from, src, dest, context);
        createdFileNames.add(dest);
    }

    @Override
    public void rename(String source, String dest) throws IOException {
        in.rename(source, dest);
        synchronized (createdFileNames) {
            createdFileNames.add(dest);
            createdFileNames.remove(source);
        }
    }

    /** NOTE: returns a copy of the created files. */
    public Set<String> getCreatedFiles() {
        return new HashSet<>(createdFileNames);
    }

    public void clearCreatedFiles() {
        createdFileNames.clear();
    }
}
