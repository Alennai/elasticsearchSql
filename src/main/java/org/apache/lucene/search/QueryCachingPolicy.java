package org.apache.lucene.search;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface QueryCachingPolicy  {

    /** A simple policy that caches all the provided filters on all segments. */
    public static final QueryCachingPolicy ALWAYS_CACHE = new QueryCachingPolicy() {

        @Override
        public void onUse(Query query) {}

        @Override
        public boolean shouldCache(Query query) throws IOException {
            return true;
        }

    };

    /** Callback that is called every time that a cached filter is used.
     *  This is typically useful if the policy wants to track usage statistics
     *  in order to make decisions. */
    void onUse(Query query);

    /** Whether the given {@link Query} is worth caching.
     *  This method will be called by the {@link QueryCache} to know whether to
     *  cache. It will first attempt to load a {@link DocIdSet} from the cache.
     *  If it is not cached yet and this method returns <tt>true</tt> then a
     *  cache entry will be generated. Otherwise an uncached scorer will be
     *  returned. */
    boolean shouldCache(Query query) throws IOException;

}

