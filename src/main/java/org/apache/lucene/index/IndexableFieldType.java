package org.apache.lucene.index;

/**
 * Created by xusiao on 2018/5/7.
 */
public interface IndexableFieldType  {

    /** True if the field's value should be stored */
    public boolean stored();

    /**
     * True if this field's value should be analyzed by the
     * {@link }.
     * <p>
     * This has no effect if {@link #indexOptions()} returns
     * IndexOptions.NONE.
     */
    // TODO: shouldn't we remove this?  Whether/how a field is
    // tokenized is an impl detail under Field?
    public boolean tokenized();

    /**
     * True if this field's indexed form should be also stored
     * into term vectors.
     * <p>
     * This builds a miniature inverted-index for this field which
     * can be accessed in a document-oriented way from
     * {@link #(int,String)}.
     * <p>
     * This option is illegal if {@link #indexOptions()} returns
     * IndexOptions.NONE.
     */
    public boolean storeTermVectors();

    /**
     * True if this field's token character offsets should also
     * be stored into term vectors.
     * <p>
     * This option is illegal if term vectors are not enabled for the field
     * ({@link #storeTermVectors()} is false)
     */
    public boolean storeTermVectorOffsets();

    /**
     * True if this field's token positions should also be stored
     * into the term vectors.
     * <p>
     * This option is illegal if term vectors are not enabled for the field
     * ({@link #storeTermVectors()} is false).
     */
    public boolean storeTermVectorPositions();

    /**
     * True if this field's token payloads should also be stored
     * into the term vectors.
     * <p>
     * This option is illegal if term vector positions are not enabled
     * for the field ({@link #storeTermVectors()} is false).
     */
    public boolean storeTermVectorPayloads();

    /**
     * True if normalization values should be omitted for the field.
     * <p>
     * This saves memory, but at the expense of scoring quality (length normalization
     * will be disabled), and if you omit norms, you cannot use index-time boosts.
     */
    public boolean omitNorms();

    /** {@link }, describing what should be
     *  recorded into the inverted index */
    public IndexOptions indexOptions();

    /**
     * DocValues {@link }: how the field's value will be indexed
     * into docValues.
     */
    public DocValuesType docValuesType();

    /**
     * If this is positive, the field is indexed as a point.
     */
    public int pointDimensionCount();

    /**
     * The number of bytes in each dimension's values.
     */
    public int pointNumBytes();
}

