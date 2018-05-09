package org.apache.lucene.analysis;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class CharFilter extends Reader {
    /**
     * The underlying character-input stream.
     */
    protected final Reader input;

    /**
     * Create a new CharFilter wrapping the provided reader.
     * @param input a Reader, can also be a CharFilter for chaining.
     */
    public CharFilter(Reader input) {
        super(input);
        this.input = input;
    }

    /**
     * Closes the underlying input stream.
     * <p>
     * <b>NOTE:</b>
     * The default implementation closes the input Reader, so
     * be sure to call <code>super.close()</code> when overriding this method.
     */
    @Override
    public void close() throws IOException {
        input.close();
    }

    /**
     * Subclasses override to correct the current offset.
     *
     * @param currentOff current offset
     * @return corrected offset
     */
    protected abstract int correct(int currentOff);

    /**
     * Chains the corrected offset through the input
     * CharFilter(s).
     */
    public final int correctOffset(int currentOff) {
        final int corrected = correct(currentOff);
        return (input instanceof CharFilter) ? ((CharFilter) input).correctOffset(corrected) : corrected;
    }
}
