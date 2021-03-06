/*

  @author shaco.zhu
 * Date:2017年5月25日下午1:31:35

 */
package org.parc.restes.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ESResult {
    private int total;
    private String scrollid;
    private List<JSONObject> documents;
    private JSONObject aggregation;

    public JSONObject getAggregation() {
        return aggregation;
    }

    void setAggregation(JSONObject aggregation) {
        this.aggregation = aggregation;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the documents
     */
    public List<JSONObject> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    void setDocuments(List<JSONObject> documents) {
        this.documents = documents;
    }

    public String getScrollid() {
        return scrollid;
    }

    void setScrollid(String scrollid) {
        this.scrollid = scrollid;
    }
}
