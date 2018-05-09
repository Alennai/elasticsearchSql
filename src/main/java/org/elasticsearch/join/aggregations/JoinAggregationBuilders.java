package org.elasticsearch.join.aggregations;

/**
 * Created by xusiao on 2018/5/7.
 */
public class JoinAggregationBuilders {
    /**
     * Create a new {@link Children} aggregation with the given name.
     */
    public static ChildrenAggregationBuilder children(String name, String childType) {
        return new ChildrenAggregationBuilder(name, childType);
    }
}
