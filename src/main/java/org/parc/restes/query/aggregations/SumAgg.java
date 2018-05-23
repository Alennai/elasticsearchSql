package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONObject;
import com.dbapp.cpsysportal.elasticsearch.query.Aggregation;

public class SumAgg extends Aggregation {
    private static final String agg_operator="sum";
    public SumAgg(String aggName) {
        super(aggName);
        aggContent.put(agg_operator,new JSONObject());
    }

    public SumAgg field(String field) {
        aggContent.getJSONObject(agg_operator).put("field", field);
        return this;
    }

    @Override
    public SumAgg subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }
}
