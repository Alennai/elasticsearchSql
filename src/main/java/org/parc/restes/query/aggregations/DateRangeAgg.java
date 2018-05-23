package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dbapp.cpsysportal.elasticsearch.query.Aggregation;

public class DateRangeAgg extends Aggregation {
    private static final String agg_operator="date_range";

    public DateRangeAgg(String aggName) {
        super(aggName);
        aggContent.put("date_range",new JSONObject());
    }

    public DateRangeAgg field(String field) {
        aggContent.getJSONObject(agg_operator).put("field", field);
        return this;
    }

    public DateRangeAgg format(String format) {
        aggContent.getJSONObject(agg_operator).put("format", format);
        return this;
    }

    public DateRangeAgg addRange(String from, String to) {
        JSONObject rangeObj = new JSONObject();
        if (from != null) {
            rangeObj.put("from", from);
        }
        if (to != null) {
            rangeObj.put("to", to);
        }
        if ( aggContent.getJSONObject(agg_operator).containsKey("ranges")) {
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
    public DateRangeAgg subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }

}
