package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONObject;
import org.parc.restes.query.Aggregation;

public class TermsAgg extends Aggregation {
    private static final String agg_operator = "terms";

    public TermsAgg(String aggName) {
        super(aggName);
        aggContent.put(agg_operator, new JSONObject());
    }

    public TermsAgg field(String field) {
        aggContent.getJSONObject(agg_operator).put("field", field);
        return this;
    }

    public TermsAgg size(int size) {
        aggContent.getJSONObject(agg_operator).put("size", size);
        return this;
    }

    public TermsAgg includeExcludeRegex(String include, String exclude) {
        if (include != null) {
            aggContent.getJSONObject(agg_operator).put("include", include);
        }
        if (exclude != null) {
            aggContent.getJSONObject(agg_operator).put("exclude", exclude);
        }
        return this;
    }

    public TermsAgg includeExclude(String[] includes, String[] excludes) {
        if (includes != null) {
            aggContent.getJSONObject(agg_operator).put("include", includes);
        }
        if (excludes != null) {
            aggContent.getJSONObject(agg_operator).put("exclude", excludes);
        }
        return this;
    }

    public TermsAgg orderCount(boolean asc) {
        JSONObject orderObj;
        if (!asc) {
            orderObj = new JSONObject().fluentPut("_count", "desc");
        } else {
            orderObj = new JSONObject().fluentPut("_count", "aes");
        }
        aggContent.getJSONObject(agg_operator).put("order", orderObj);
        return this;
    }

    public TermsAgg orderAggregation(String aggName, boolean asc) {
        JSONObject orderObj;
        if (!asc) {
            orderObj = new JSONObject().fluentPut(aggName, "asc");
        } else {
            orderObj = new JSONObject().fluentPut(aggName, "desc");
        }
        return this;
    }

    @Override
    public TermsAgg subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }


}
