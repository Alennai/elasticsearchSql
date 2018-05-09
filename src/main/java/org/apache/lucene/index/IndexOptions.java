package org.apache.lucene.index;

/**
 * Created by xusiao on 2018/5/7.
 */
public enum IndexOptions  {
    // NOTE: order is important here; FieldInfo uses this
    // order to merge two conflicting IndexOptions (always
    // "downgrades" by picking the lowest).
    /** Not indexed */
    NONE,
    /**
     * Only documents are indexed: term frequencies and positions are omitted.
     * Phrase and other positional queries on the field will throw an exception, and scoring
     * will behave as if any term in the document appears only once.
     */
    DOCS,
    /**
     * Only documents and term frequencies are indexed: positions are omitted.
     * This enables normal scoring, except Phrase and other positional queries
     * will throw an exception.
     */
    DOCS_AND_FREQS,
    /**
     * Indexes documents, frequencies and positions.
     * This is a typical default for full-text search: full scoring is enabled
     * and positional queries are supported.
     */
    DOCS_AND_FREQS_AND_POSITIONS,
    /**
     * Indexes documents, frequencies, positions and offsets.
     * Character offsets are encoded alongside the positions.
     */
    DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS,
}
