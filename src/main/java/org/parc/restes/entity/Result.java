/**
 * @author shaco.zhu
 * Date:2017年5月25日下午1:31:35
 */
package org.parc.restes.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Stack;

public class Result extends ESResult {
    private Stack<JSONObject> stack;

    public Stack<JSONObject> getStack() {
        return stack;
    }

    public void setStack(Stack<JSONObject> stack) {
        this.stack = stack;
    }

    public Result total(int total) {
        this.setTotal(total);
        return this;
    }

    public Result documents(List<JSONObject> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Result stack(Stack<JSONObject> documents) {
        this.stack = documents;
        return this;
    }

    public Result scrollid(String scrollid) {
        this.setScrollid(scrollid);
        return this;
    }

    public Result aggregation(JSONObject agg) {
        this.setAggregation(agg);
        return this;
    }

}
