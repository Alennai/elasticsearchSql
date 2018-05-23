package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.parc.restes.query.Aggregation;

public class RangeAgg extends Aggregation {
    private static final String agg_operator="range";
    public RangeAgg(String aggName) {
        super(aggName);
        aggContent.put(agg_operator,new JSONObject());
    }

    public RangeAgg field(String field) {
        aggContent.getJSONObject(agg_operator).put("field", field);
        return this;
    }

    public RangeAgg addRange(Integer from, Integer to) {
        JSONObject rangeObj = new JSONObject();
        if (from != null) {
            rangeObj.put("from", from);
        }
        if (to != null) {
            rangeObj.put("to", to);
        }
        if (aggContent.getJSONObject(agg_operator).containsKey("ranges")) {
            JSONArray ranges = aggContent.getJSONObject(agg_operator).getJSONArray("ranges");
            ranges.add(rangeObj);
        } else {
            JSONArray ranges = new JSONArray();
            ranges.add(rangeObj);
            aggContent.getJSONObject(agg_operator).put("ranges", ranges);
        }
        return this;
    }

    @Override
    public RangeAgg subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }
}
