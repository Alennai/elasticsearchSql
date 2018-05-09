package org.apache.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.BytesRef;

import java.io.Reader;

/**
 * Created by xusiao on 2018/5/7.
 */
public interface IndexableField {

    /** Field name */
    public String name();

    /** {@link } describing the properties
     * of this field. */
    public IndexableFieldType fieldType();

    /**
     * Creates the TokenStream used for indexing this field.  If appropriate,
     * implementations should use the given Analyzer to create the TokenStreams.
     *
     * @param analyzer Analyzer that should be used to create the TokenStreams from
     * @param reuse TokenStream for a previous instance of this field <b>name</b>. This allows
     *              custom field types (like StringField and NumericField) that do not use
     *              the analyzer to still have good performance. Note: the passed-in type
     *              may be inappropriate, for example if you mix up different types of Fields
     *              for the same field name. So it's the responsibility of the implementation to
     *              check.
     * @return TokenStream value for indexing the document.  Should always return
     *         a non-null value if the field is to be indexed
     */
    public TokenStream tokenStream(Analyzer analyzer, TokenStream reuse);

    /**
     * Returns the field's index-time boost.
     * <p>
     * Only fields can have an index-time boost, if you want to simulate
     * a "document boost", then you must pre-multiply it across all the
     * relevant fields yourself.
     * <p>The boost is used to compute the norm factor for the field.  By
     * default, in the {@link )} method,
     * the boost value is multiplied by the length normalization factor and then
     * rounded by {@link float)} before it is stored in the
     * index.  One should attempt to ensure that this product does not overflow
     * the range of that encoding.
     * <p>
     * It is illegal to return a boost other than 1.0f for a field that is not
     * indexed ({@link IndexableFieldType#indexOptions()} is IndexOptions.NONE) or
     * omits normalization values ({@link IndexableFieldType#omitNorms()} returns true).
     *
     * @see
     * @see
     * @deprecated Index-time boosts are deprecated, please index index-time scoring
     *             factors into a doc value field and combine them with the score at
     *             query time using eg. FunctionScoreQuery.
     */
    @Deprecated
    public float boost();

    /** Non-null if this field has a binary value */
    public BytesRef binaryValue();

    /** Non-null if this field has a string value */
    public String stringValue();

    /** Non-null if this field has a Reader value */
    public Reader readerValue();

    /** Non-null if this field has a numeric value */
    public Number numericValue();
}

