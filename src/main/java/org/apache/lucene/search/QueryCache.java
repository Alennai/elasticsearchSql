package org.apache.lucene.search;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface QueryCache  {

    /**
     * Return a wrapper around the provided <code>weight</code> that will cache
     * matching docs per-segment accordingly to the given <code>policy</code>.
     * NOTE: The returned weight will only be equivalent if scores are not needed.
     * @see Collector#needsScores()
     */
    Weight doCache(Weight weight, QueryCachingPolicy policy);

}
