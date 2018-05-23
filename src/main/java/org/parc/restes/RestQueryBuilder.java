package org.parc.restes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dbapp.cpsysportal.elasticsearch.query.AggregationFactory;
import com.dbapp.cpsysportal.elasticsearch.query.Aggregation;
import com.dbapp.cpsysportal.elasticsearch.query.aggregations.DateHistogram;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;

/**
 * Created by xusiao on 2018/5/11.
 */
public class RestQueryBuilder {
    private JSONObject restJson = new JSONObject();

    public void setQuery(QueryBuilder query) {
        restJson.put("query", JSONObject.parse(query.toString()));
    }

    public void setFetchSource(String[] includes, String[] excludes) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("includes", setSource(includes));
        jsonObject.put("excludes", setSource(excludes));
        restJson.put("_source", jsonObject);

    }

    private Object setSource(String[] includes) {
        if (includes != null) {
            return includes;
        } else return new java.util.ArrayList<>();
    }

    public void setSize(int size) {
        restJson.put("size", size);
    }

    public void setScroll(TimeValue timeValue) {

    }

    @Override
    public String toString() {
        return restJson.toString();
    }

    public void addScriptField(String timestamp, String script) {
        JSONObject scriptJson = new JSONObject();
        scriptJson.put("source", script);
        scriptJson.put("lang", "painless");
        JSONObject fieldJson = new JSONObject();
        fieldJson.put("script", scriptJson);
        fieldJson.put("ignore_failure", false);
        JSONObject scriptFieldJson = new JSONObject();
        scriptFieldJson.put(timestamp, fieldJson);
        restJson.put("script_fields", scriptFieldJson);
    }

    public void storedFields(String[] fields) {
        restJson.put("stored_fields", setSource(fields));
    }

    public void addAggregations(Aggregation aggregation) {
        restJson.put("aggregations", JSONObject.parseObject( aggregation.toString()));
    }


    public static void main(String[] args) {

        String str = AggregationFactory.dateHistogram("hist")
                .field("@timestamp")
                .dateHistogramInterval(DateHistogram.DateHistogramInterval.hours(1))
                .timeZone(DateTimeZone.forID("Asia/Shanghai"))
                .keyOrder(true)
                .subAggregation(AggregationFactory.terms("terms")
                        .field("TERM").size(200).orderCount(false)
                ).toString();

        System.out.println(str);
    }

    public void addSort(String rangeTime, SortOrder sortOrder) {
        JSONObject orderJson=new JSONObject();
        orderJson.put("order",sortOrder);
        JSONObject fieldJson=new JSONObject();;
        fieldJson.put(rangeTime,orderJson);
        JSONArray sortArr=new JSONArray();
        sortArr.add(fieldJson);
        restJson.put("sort",sortArr);
    }
}
