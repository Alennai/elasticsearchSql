package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONObject;
import org.parc.restes.query.Aggregation;

/**
 * @author delin
 * @date 2018/5/21
 */
public class OperatorAgg extends Aggregation {
    //默认算子为sum
    private String operator = "sum";
    public OperatorAgg(String aggName, String operator) {
        super(aggName);
        aggContent.put(operator,new JSONObject());
        this.operator = operator;
    }

    public OperatorAgg field(String field) {
        aggContent.getJSONObject(operator).put("field", field);
        return this;
    }

    @Override
    public OperatorAgg subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }
}
