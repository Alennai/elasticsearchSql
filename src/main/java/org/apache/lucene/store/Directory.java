package org.apache.lucene.store;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Collection;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class Directory implements Closeable {

    /**
     * Returns an array of strings, one for each entry in the directory, in sorted (UTF16, java's String.compare) order.
     *
     * @throws IOException in case of IO error
     */
    public abstract String[] listAll() throws IOException;

    /** Removes an existing file in the directory. */
    public abstract void deleteFile(String name) throws IOException;

    /**
     * Returns the length of a file in the directory. This method follows the
     * following contract:
     * <ul>
     * <li>Throws {@link FileNotFoundException} or {@link NoSuchFileException}
     * if the file does not exist.
     * <li>Returns a value &ge;0 if the file exists, which specifies its length.
     * </ul>
     *
     * @param name the name of the file for which to return the length.
     * @throws IOException if there was an IO error while retrieving the file's
     *         length.
     */
    public abstract long fileLength(String name) throws IOException;

    /** Creates a new, empty file in the directory with the given name.
     Returns a stream writing this file. */
    public abstract IndexOutput createOutput(String name, IOContext context) throws IOException;

    /** Creates a new, empty file for writing in the directory, with a
     *  temporary file name including prefix and suffix, ending with the
     *  reserved extension <code>.tmp</code>.  Use
     *  {@link IndexOutput#getName} to see what name was used.  */
    public abstract IndexOutput createTempOutput(String prefix, String suffix, IOContext context) throws IOException;

    /**
     * Ensure that any writes to these files are moved to
     * stable storage.  Lucene uses this to properly commit
     * changes to the index, to prevent a machine/OS crash
     * from corrupting the index.
     * <br>
     * NOTE: Clients may call this method for same files over
     * and over again, so some impls might optimize for that.
     * For other impls the operation can be a noop, for various
     * reasons.
     */
    public abstract void sync(Collection<String> names) throws IOException;

    /**
     * Renames {@code source} to {@code dest} as an atomic operation,
     * where {@code dest} does not yet exist in the directory.
     * <p>
     * Notes: This method is used by IndexWriter to publish commits.
     * It is ok if this operation is not truly atomic, for example
     * both {@code source} and {@code dest} can be visible temporarily.
     * It is just important that the contents of {@code dest} appear
     * atomically, or an exception is thrown.
     *
     * @deprecated Use {@link #rename} and {@link #syncMetaData} instead.
     */
    @Deprecated
    public final void renameFile(String source, String dest) throws IOException {
        rename(source, dest);
        syncMetaData();
    }

    /**
     * Renames {@code source} to {@code dest} as an atomic operation,
     * where {@code dest} does not yet exist in the directory.
     * <p>
     * Notes: This method is used by IndexWriter to publish commits.
     * It is ok if this operation is not truly atomic, for example
     * both {@code source} and {@code dest} can be visible temporarily.
     * It is just important that the contents of {@code dest} appear
     * atomically, or an exception is thrown.
     */
    public abstract void rename(String source, String dest) throws IOException;

    /**
     * Ensure that directory metadata, such as recent file renames, are made
     * durable.
     */
    public abstract void syncMetaData() throws IOException;

    /** Returns a stream reading an existing file.
     * <p>Throws {@link FileNotFoundException} or {@link NoSuchFileException}
     * if the file does not exist.
     */
    public abstract IndexInput openInput(String name, IOContext context) throws IOException;

    /** Returns a stream reading an existing file, computing checksum as it reads */
    public ChecksumIndexInput openChecksumInput(String name, IOContext context) throws IOException {
        return new BufferedChecksumIndexInput(openInput(name, context));
    }

    /**
     * Returns an obtained {@link Lock}.
     * @param name the name of the lock file
     * @throws LockObtainFailedException (optional specific exception) if the lock could
     *         not be obtained because it is currently held elsewhere.
     * @throws IOException if any i/o error occurs attempting to gain the lock
     */
    public abstract Lock obtainLock(String name) throws IOException;

    /** Closes the store. */
    @Override
    public abstract void close() throws IOException;

    @Override
    public String toString() {
        return getClass().getSimpleName() + '@' + Integer.toHexString(hashCode());
    }

    /**
     * Copies the file <i>src</i> in <i>from</i> to this directory under the new
     * file name <i>dest</i>.
     * <p>
     * If you want to copy the entire source directory to the destination one, you
     * can do so like this:
     *
     * <pre class="prettyprint">
     * Directory to; // the directory to copy to
     * for (String file : dir.listAll()) {
     *   to.copyFrom(dir, file, newFile, IOContext.DEFAULT); // newFile can be either file, or a new name
     * }
     * </pre>
     * <p>
     * <b>NOTE:</b> this method does not check whether <i>dest</i> exist and will
     * overwrite it if it does.
     */
    public void copyFrom(Directory from, String src, String dest, IOContext context) throws IOException {
        boolean success = false;
        try (IndexInput is = from.openInput(src, context);
             IndexOutput os = createOutput(dest, context)) {
            os.copyBytes(is, is.length());
            success = true;
        } finally {
            if (!success) {
                IOUtils.deleteFilesIgnoringExceptions(this, dest);
            }
        }
    }

    /**
     * @throws AlreadyClosedException if this Directory is closed
     */
    protected void ensureOpen() throws AlreadyClosedException {}
}
