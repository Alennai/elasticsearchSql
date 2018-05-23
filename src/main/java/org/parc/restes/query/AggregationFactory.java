package org.parc.restes.query;

import com.dbapp.cpsysportal.elasticsearch.query.aggregations.*;

public class AggregationFactory {


    public static CardinalityAgg cardinality(String aggName) {
        return new CardinalityAgg(aggName);
    }

    public static DateHistogram dateHistogram(String aggName) {
        return new DateHistogram(aggName);
    }

    public static DateRangeAgg dateRange(String aggName) {
        return new DateRangeAgg(aggName);
    }

    public static RangeAgg range(String aggName) {
        return new RangeAgg(aggName);
    }

    public static SumAgg sum(String aggName) {
        return new SumAgg(aggName);
    }

    public static TermsAgg terms(String aggName) {
        return new TermsAgg(aggName);
    }

    public static Histogram histogram(String aggName){return new Histogram(aggName);}

    public static OperatorAgg operator(String aggName, String operator) {
        return new OperatorAgg(aggName,operator);
    }

}
