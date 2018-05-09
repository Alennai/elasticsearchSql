package org.apache.lucene.search;

import org.apache.lucene.index.IndexReader;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class Query {

    /** Prints a query to a string, with <code>field</code> assumed to be the
     * default field and omitted.
     */
    public abstract String toString(String field);

    /** Prints a query to a string. */
    @Override
    public final String toString() {
        return toString("");
    }

    /**
     * Expert: Constructs an appropriate Weight implementation for this query.
     * <p>
     * Only implemented by primitive queries, which re-write to themselves.
     *
     * @param needsScores   True if document scores ({@link Scorer#score}) or match
     *                      frequencies ({@link Scorer#freq}) are needed.
     */
    public Weight createWeight(IndexSearcher searcher, boolean needsScores) throws IOException {
        throw new UnsupportedOperationException("Query " + this + " does not implement createWeight");
    }

    /** Expert: called to re-write queries into primitive queries. For example,
     * a PrefixQuery will be rewritten into a BooleanQuery that consists
     * of TermQuerys.
     */
    public Query rewrite(IndexReader reader) throws IOException {
        return this;
    }

    /**
     * Override and implement query instance equivalence properly in a subclass.
     * This is required so that {@link } works properly.
     *
     * Typically a query will be equal to another only if it's an instance of
     * the same class and its document-filtering properties are identical that other
     * instance. Utility methods are provided for certain repetitive code.
     *
     * @see #sameClassAs(Object)
     * @see #classHash()
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Override and implement query hash code properly in a subclass.
     * This is required so that {@link } works properly.
     *
     * @see #equals(Object)
     */
    @Override
    public abstract int hashCode();

    /**
     * Utility method to check whether <code>other</code> is not null and is exactly
     * of the same class as this object's class.
     *
     * When this method is used in an implementation of {@link #equals(Object)},
     * consider using {@link #classHash()} in the implementation
     * of {@link #hashCode} to differentiate different class
     */
    protected final boolean sameClassAs(Object other) {
        return other != null && getClass() == other.getClass();
    }

    private final int CLASS_NAME_HASH = getClass().getName().hashCode();

    /**
     * Provides a constant integer for a given class, derived from the name of the class.
     * The rationale for not using just {@link Class#hashCode()} is that classes may be
     * assigned different hash codes for each execution and we want hashes to be possibly
     * consistent to facilitate debugging.
     */
    protected final int classHash() {
        return CLASS_NAME_HASH;
    }
}
