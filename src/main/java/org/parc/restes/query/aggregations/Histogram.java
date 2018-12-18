package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONObject;
import org.parc.restes.query.Aggregation;

public class Histogram extends Aggregation {
    private static final String agg_operator = "histogram";

    public Histogram(String aggName) {
        super(aggName);
        aggContent.put(agg_operator, new JSONObject());
    }

    public Histogram interval(double interval) {
        aggContent.getJSONObject(agg_operator).put("interval", interval);
        return this;
    }

    public Histogram min_doc_count(double min_doc_count) {
        aggContent.getJSONObject(agg_operator).put("min_doc_count", min_doc_count);
        return this;
    }

    public Histogram missing(double missing) {
        aggContent.getJSONObject(agg_operator).put("missing", missing);
        return this;
    }

    public Histogram keyOrder(boolean asc) {
        if (asc)
            aggContent.getJSONObject(agg_operator).put("order", new JSONObject().fluentPut("_key", "aes"));
        else
            aggContent.getJSONObject(agg_operator).put("order", new JSONObject().fluentPut("_key", "desc"));
        return this;
    }

    public Histogram field(String field) {
        aggContent.getJSONObject(agg_operator).put("field", field);
        return this;
    }

    @Override
    public Histogram subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }
}
