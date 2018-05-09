package org.apache.lucene.search;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by xusiao on 2018/5/8.
 */
public class Sort  {

    /**
     * Represents sorting by computed relevance. Using this sort criteria returns
     * the same results as calling
     * {@link IndexSearcher#search(Query,int) IndexSearcher#search()}without a sort criteria,
     * only with slightly more overhead.
     */
    public static final Sort RELEVANCE = new Sort();

    /** Represents sorting by index order. */
    public static final Sort INDEXORDER = new Sort(SortField.FIELD_DOC);

    // internal representation of the sort criteria
    SortField[] fields;

    /**
     * Sorts by computed relevance. This is the same sort criteria as calling
     * {@link IndexSearcher#search(Query,int) IndexSearcher#search()}without a sort criteria,
     * only with slightly more overhead.
     */
    public Sort() {
        this(SortField.FIELD_SCORE);
    }

    /** Sorts by the criteria in the given SortField. */
    public Sort(SortField field) {
        setSort(field);
    }

    /** Sets the sort to the given criteria in succession: the
     *  first SortField is checked first, but if it produces a
     *  tie, then the second SortField is used to break the tie,
     *  etc.  Finally, if there is still a tie after all SortFields
     *  are checked, the internal Lucene docid is used to break it. */
    public Sort(SortField... fields) {
        setSort(fields);
    }

    /** Sets the sort to the given criteria. */
    public void setSort(SortField field) {
        this.fields = new SortField[] { field };
    }

    /** Sets the sort to the given criteria in succession: the
     *  first SortField is checked first, but if it produces a
     *  tie, then the second SortField is used to break the tie,
     *  etc.  Finally, if there is still a tie after all SortFields
     *  are checked, the internal Lucene docid is used to break it. */
    public void setSort(SortField... fields) {
        if (fields.length == 0) {
            throw new IllegalArgumentException("There must be at least 1 sort field");
        }
        this.fields = fields;
    }

    /**
     * Representation of the sort criteria.
     * @return Array of SortField objects used in this sort criteria
     */
    public SortField[] getSort() {
        return fields;
    }

    /**
     * Rewrites the SortFields in this Sort, returning a new Sort if any of the fields
     * changes during their rewriting.
     *
     * @param searcher IndexSearcher to use in the rewriting
     * @return {@code this} if the Sort/Fields have not changed, or a new Sort if there
     *        is a change
     * @throws IOException Can be thrown by the rewriting
     * @lucene.experimental
     */
    public Sort rewrite(IndexSearcher searcher) throws IOException {
        boolean changed = false;

        SortField[] rewrittenSortFields = new SortField[fields.length];
        for (int i = 0; i < fields.length; i++) {
            rewrittenSortFields[i] = fields[i].rewrite(searcher);
            if (fields[i] != rewrittenSortFields[i]) {
                changed = true;
            }
        }

        return (changed) ? new Sort(rewrittenSortFields) : this;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < fields.length; i++) {
            buffer.append(fields[i].toString());
            if ((i+1) < fields.length)
                buffer.append(',');
        }

        return buffer.toString();
    }

    /** Returns true if <code>o</code> is equal to this. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sort)) return false;
        final Sort other = (Sort)o;
        return Arrays.equals(this.fields, other.fields);
    }

    /** Returns a hash code value for this object. */
    @Override
    public int hashCode() {
        return 0x45aaf665 + Arrays.hashCode(fields);
    }

    /** Returns true if the relevance score is needed to sort documents. */
    public boolean needsScores() {
        for (SortField sortField : fields) {
            if (sortField.needsScores()) {
                return true;
            }
        }
        return false;
    }

}
