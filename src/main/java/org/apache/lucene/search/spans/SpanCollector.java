package org.apache.lucene.search.spans;

import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface SpanCollector  {

    /**
     * Collect information from postings
     * @param postings a {@link PostingsEnum}
     * @param position the position of the PostingsEnum
     * @param term     the {@link Term} for this postings list
     * @throws IOException on error
     */
    public void collectLeaf(PostingsEnum postings, int position, Term term) throws IOException;

    /**
     * Call to indicate that the driving Spans has moved to a new position
     */
    public void reset();

}
