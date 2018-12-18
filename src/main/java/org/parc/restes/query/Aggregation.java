package org.parc.restes.query;

import com.alibaba.fastjson.JSONObject;

public abstract class Aggregation {
    protected JSONObject aggContent;
    private String aggName;
    private JSONObject content;

    protected Aggregation(String aggName) {
        aggContent = new JSONObject();
        content = new JSONObject();
        this.aggName = aggName;
        content.put(aggName, aggContent);
    }

    public static Aggregation nested(String nestedAggName, String nestedPath) {
        return null;
    }

    public String getAggName() {

        return aggName;
    }

    public void setAggName(String aggName) {
        this.aggName = aggName;
    }

    public JSONObject getContent() {
        return content;
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
}
