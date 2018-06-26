package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONObject;
import org.parc.restes.query.Aggregation;

public class CardinalityAgg extends Aggregation {
    private static final String agg_operator="cardinality";
    public CardinalityAgg(String aggName) {
        super(aggName);
        aggContent.put(agg_operator,new JSONObject());
    }

    public CardinalityAgg field(String field) {
        aggContent.getJSONObject(agg_operator).put("field", field);
        return this;
    }

    public CardinalityAgg precisionThreshold(long precisionThreshold) {
        aggContent.getJSONObject(agg_operator).put("precision_threshold", precisionThreshold);
        return this;
    }

    @Override
    public CardinalityAgg subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }

}
