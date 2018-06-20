package org.parc.restes.query;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.search.aggregations.BucketOrder;

public abstract class Aggregation {
    protected String aggName;
    protected JSONObject aggContent;
    protected JSONObject content;

    public Aggregation(String aggName) {
        aggContent = new JSONObject();
        content=new JSONObject();
        this.aggName = aggName;
        content.put(aggName, aggContent);
    }

    public String getAggName() {

        return aggName;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setAggName(String aggName) {
        this.aggName = aggName;
    }

    public JSONObject getAggContent() {
        return aggContent;
    }

    public void setAggContent(JSONObject aggContent) {
        this.aggContent = aggContent;
    }

    @Override
    public String toString() {
        return content.toString();
    }

    public abstract Aggregation subAggregation(Aggregation aggregation);

    public abstract void order(BucketOrder key);

    public static Aggregation nested(String nestedAggName, String nestedPath) {
        return null;
    }
}
