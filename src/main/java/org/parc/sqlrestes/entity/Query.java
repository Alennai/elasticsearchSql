package org.parc.sqlrestes.entity;

/**
 * Created by xusiao on 2018/5/4.
 */
abstract class Query {
    private final int CLASS_NAME_HASH = this.getClass().getName().hashCode();

    Query() {
    }

    protected abstract String toString(String var1);

    @Override
    public final String toString() {
        return this.toString("");
    }

//    public Weight createWeight(IndexSearcher searcher, boolean needsScores, float boost) throws IOException {
//        throw new UnsupportedOperationException("Query " + this + " does not implement createWeight");
//    }

//    public Query rewrite(IndexReader reader) throws IOException {
//        return this;
//    }

    @Override
    public abstract boolean equals(Object var1);

    @Override
    public abstract int hashCode();

    protected final boolean sameClassAs(Object other) {
        return other != null && this.getClass() == other.getClass();
    }

    protected final int classHash() {
        return this.CLASS_NAME_HASH;
    }
}

