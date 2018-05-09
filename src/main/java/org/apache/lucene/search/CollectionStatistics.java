package org.apache.lucene.search;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;

/**
 * Created by xusiao on 2018/5/8.
 */
public class CollectionStatistics  {
    private final String field;
    private final long maxDoc;
    private final long docCount;
    private final long sumTotalTermFreq;
    private final long sumDocFreq;

    public CollectionStatistics(String field, long maxDoc, long docCount, long sumTotalTermFreq, long sumDocFreq) {
        assert maxDoc >= 0;
        assert docCount >= -1 && docCount <= maxDoc; // #docs with field must be <= #docs
        assert sumDocFreq == -1 || sumDocFreq >= docCount; // #postings must be >= #docs with field
        assert sumTotalTermFreq == -1 || sumTotalTermFreq >= sumDocFreq; // #positions must be >= #postings
        this.field = field;
        this.maxDoc = maxDoc;
        this.docCount = docCount;
        this.sumTotalTermFreq = sumTotalTermFreq;
        this.sumDocFreq = sumDocFreq;
    }

    /** returns the field name */
    public final String field() {
        return field;
    }

    /** returns the total number of documents, regardless of
     * whether they all contain values for this field.
     * @see IndexReader#maxDoc() */
    public final long maxDoc() {
        return maxDoc;
    }

    /** returns the total number of documents that
     * have at least one term for this field.
     * @see Terms#getDocCount() */
    public final long docCount() {
        return docCount;
    }

    /** returns the total number of tokens for this field
     * @see Terms#getSumTotalTermFreq() */
    public final long sumTotalTermFreq() {
        return sumTotalTermFreq;
    }

    /** returns the total number of postings for this field
     * @see Terms#getSumDocFreq() */
    public final long sumDocFreq() {
        return sumDocFreq;
    }
}

