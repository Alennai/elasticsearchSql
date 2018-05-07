package org.elasticsearch.seart.sort;

/**
 * Created by xusiao on 2018/5/3.
 */
public enum SearchType{
    DFS_QUERY_THEN_FETCH((byte)0),
    QUERY_THEN_FETCH((byte)1),
    /** @deprecated */
    @Deprecated
    QUERY_AND_FETCH((byte)3);

    public static final SearchType DEFAULT;
    private byte id;

    private SearchType(byte id) {
        this.id = id;
    }

    public byte id() {
        return this.id;
    }

    public static SearchType fromId(byte id) {
        if(id == 0) {
            return DFS_QUERY_THEN_FETCH;
        } else if(id != 1 && id != 3) {
            throw new IllegalArgumentException("No search type for [" + id + "]");
        } else {
            return QUERY_THEN_FETCH;
        }
    }

    public static SearchType fromString(String searchType) {
        if(searchType == null) {
            return DEFAULT;
        } else if("dfs_query_then_fetch".equals(searchType)) {
            return DFS_QUERY_THEN_FETCH;
        } else if("query_then_fetch".equals(searchType)) {
            return QUERY_THEN_FETCH;
        } else {
            throw new IllegalArgumentException("No search type for [" + searchType + "]");
        }
    }

    static {
        DEFAULT = QUERY_THEN_FETCH;
    }
}
