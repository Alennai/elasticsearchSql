package org.apache.lucene.search;

import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

/**
 * Created by xusiao on 2018/5/8.
 */
public class TermStatistics {
    private final BytesRef term;
    private final long docFreq;
    private final long totalTermFreq;

    public TermStatistics(BytesRef term, long docFreq, long totalTermFreq) {
        assert docFreq >= 0;
        assert totalTermFreq == -1 || totalTermFreq >= docFreq; // #positions must be >= #postings
        this.term = term;
        this.docFreq = docFreq;
        this.totalTermFreq = totalTermFreq;
    }

    /** returns the term text */
    public final BytesRef term() {
        return term;
    }

    /** returns the number of documents this term occurs in
     * @see TermsEnum#docFreq() */
    public final long docFreq() {
        return docFreq;
    }

    /** returns the total number of occurrences of this term
     * @see TermsEnum#totalTermFreq() */
    public final long totalTermFreq() {
        return totalTermFreq;
    }
}

