package org.apache.lucene.analysis;

import org.apache.lucene.analysis.tokenattributes.PackedTokenAttributeImpl;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.util.AttributeSource;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * Created by xusiao on 2018/5/7.
 */
public abstract class TokenStream  extends AttributeSource implements Closeable {

    /** Default {@link AttributeFactory} instance that should be used for TokenStreams. */
    public static final AttributeFactory DEFAULT_TOKEN_ATTRIBUTE_FACTORY =
            AttributeFactory.getStaticImplementation(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, PackedTokenAttributeImpl.class);

    /**
     * A TokenStream using the default attribute factory.
     */
    protected TokenStream() {
        super(DEFAULT_TOKEN_ATTRIBUTE_FACTORY);
        assert assertFinal();
    }

    /**
     * A TokenStream that uses the same attributes as the supplied one.
     */
    protected TokenStream(AttributeSource input) {
        super(input);
        assert assertFinal();
    }

    /**
     * A TokenStream using the supplied AttributeFactory for creating new {@link } instances.
     */
    protected TokenStream(AttributeFactory factory) {
        super(factory);
        assert assertFinal();
    }

    private boolean assertFinal() {
        try {
            final Class<?> clazz = getClass();
            if (!clazz.desiredAssertionStatus())
                return true;
            assert clazz.isAnonymousClass() ||
                    (clazz.getModifiers() & (Modifier.FINAL | Modifier.PRIVATE)) != 0 ||
                    Modifier.isFinal(clazz.getMethod("incrementToken").getModifiers()) :
                    "TokenStream implementation classes or at least their incrementToken() implementation must be final";
            return true;
        } catch (NoSuchMethodException nsme) {
            return false;
        }
    }

    /**
     * Consumers (i.e., {@link }) use this method to advance the stream to
     * the next token. Implementing classes must implement this method and update
     * the appropriate {@link }s with the attributes of the next
     * token.
     * <P>
     * The producer must make no assumptions about the attributes after the method
     * has been returned: the caller may arbitrarily change it. If the producer
     * needs to preserve the state for subsequent calls, it can use
     * {@link #captureState} to create a copy of the current attribute state.
     * <p>
     * This method is called for every token of a document, so an efficient
     * implementation is crucial for good performance. To avoid calls to
     * {@link #addAttribute(Class)} and {@link #getAttribute(Class)},
     * references to all {@link }s that this stream uses should be
     * retrieved during instantiation.
     * <p>
     * To ensure that filters and consumers know which attributes are available,
     * the attributes must be added during instantiation. Filters and consumers
     * are not required to check for availability of attributes in
     * {@link #incrementToken()}.
     *
     * @return false for end of stream; true otherwise
     */
    public abstract boolean incrementToken() throws IOException;

    /**
     * This method is called by the consumer after the last token has been
     * consumed, after {@link #incrementToken()} returned <code>false</code>
     * (using the new <code>TokenStream</code> API). Streams implementing the old API
     * should upgrade to use this feature.
     * <p>
     * This method can be used to perform any end-of-stream operations, such as
     * setting the final offset of a stream. The final offset of a stream might
     * differ from the offset of the last token eg in case one or more whitespaces
     * followed after the last token, but a WhitespaceTokenizer was used.
     * <p>
     * Additionally any skipped positions (such as those removed by a stopfilter)
     * can be applied to the position increment, or any adjustment of other
     * attributes where the end-of-stream value may be important.
     * <p>
     * If you override this method, always call {@code super.end()}.
     *
     * @throws IOException If an I/O error occurs
     */
    public void end() throws IOException {
        endAttributes(); // LUCENE-3849: don't consume dirty atts
    }

    /**
     * This method is called by a consumer before it begins consumption using
     * {@link #incrementToken()}.
     * <p>
     * Resets this stream to a clean state. Stateful implementations must implement
     * this method so that they can be reused, just as if they had been created fresh.
     * <p>
     * If you override this method, always call {@code super.reset()}, otherwise
     * some internal state will not be correctly reset (e.g., {@link } will
     * throw {@link IllegalStateException} on further usage).
     */
    public void reset() throws IOException {}

    /** Releases resources associated with this stream.
     * <p>
     * If you override this method, always call {@code super.close()}, otherwise
     * some internal state will not be correctly reset (e.g., {@link } will
     * throw {@link IllegalStateException} on reuse).
     */
    @Override
    public void close() throws IOException {}

}

