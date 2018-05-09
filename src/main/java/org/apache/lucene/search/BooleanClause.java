package org.apache.lucene.search;

import java.util.Objects;

/**
 * Created by xusiao on 2018/5/8.
 */
public final class BooleanClause {

    /** Specifies how clauses are to occur in matching documents. */
    public static enum Occur {

        /** Use this operator for clauses that <i>must</i> appear in the matching documents. */
        MUST     { @Override public String toString() { return "+"; } },

        /** Like {@link #MUST} except that these clauses do not participate in scoring. */
        FILTER   { @Override public String toString() { return "#"; } },

        /** Use this operator for clauses that <i>should</i> appear in the
         * matching documents. For a BooleanQuery with no <code>MUST</code>
         * clauses one or more <code>SHOULD</code> clauses must match a document
         * for the BooleanQuery to match.
         * @see BooleanQuery.Builder#setMinimumNumberShouldMatch
         */
        SHOULD   { @Override public String toString() { return "";  } },

        /** Use this operator for clauses that <i>must not</i> appear in the matching documents.
         * Note that it is not possible to search for queries that only consist
         * of a <code>MUST_NOT</code> clause. These clauses do not contribute to the
         * score of documents. */
        MUST_NOT { @Override public String toString() { return "-"; } };

    }

    /** The query whose matching documents are combined by the boolean query.
     */
    private final Query query;

    private final Occur occur;


    /** Constructs a BooleanClause.
     */
    public BooleanClause(Query query, Occur occur) {
        this.query = Objects.requireNonNull(query, "Query must not be null");
        this.occur = Objects.requireNonNull(occur, "Occur must not be null");

    }

    public Occur getOccur() {
        return occur;
    }

    public Query getQuery() {
        return query;
    }

    public boolean isProhibited() {
        return Occur.MUST_NOT == occur;
    }

    public boolean isRequired() {
        return occur == Occur.MUST || occur == Occur.FILTER;
    }

    public boolean isScoring() {
        return occur == Occur.MUST || occur == Occur.SHOULD;
    }

    /** Returns true if <code>o</code> is equal to this. */
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BooleanClause))
            return false;
        BooleanClause other = (BooleanClause)o;
        return this.query.equals(other.query)
                && this.occur == other.occur;
    }

    /** Returns a hash code value for this object.*/
    @Override
    public int hashCode() {
        return 31 * query.hashCode() + occur.hashCode();
    }


    @Override
    public String toString() {
        return occur.toString() + query.toString();
    }
}

