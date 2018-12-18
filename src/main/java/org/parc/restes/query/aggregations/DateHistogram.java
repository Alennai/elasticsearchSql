package org.parc.restes.query.aggregations;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.joda.time.DateTimeZone;
import org.parc.restes.query.Aggregation;

public class DateHistogram extends Aggregation {
    private static final String agg_operator = "date_histogram";

    public DateHistogram(String aggName) {
        super(aggName);
        aggContent.put(agg_operator, new JSONObject());
    }

    public DateHistogram field(String field) {
        aggContent.getJSONObject(agg_operator).put("field", field);
        return this;
    }

    public DateHistogram extendedBounds(ExtendedBounds extendedBounds) {
        aggContent.getJSONObject(agg_operator).put("extended_bounds", extendedBounds);
        return this;
    }

    public DateHistogram minDocCount(int minDocCount) {
        aggContent.getJSONObject(agg_operator).put("min_doc_count", minDocCount);
        return this;
    }

    public DateHistogram dateHistogramInterval(DateHistogramInterval interval) {
        aggContent.getJSONObject(agg_operator).put("interval", interval.toString());
        return this;
    }

    public DateHistogram timeZone(DateTimeZone dateTimeZone) {
        aggContent.getJSONObject(agg_operator).put("time_zone", dateTimeZone.toString());
        return this;
    }

    public DateHistogram timeZone(String dateTimeZone) {
        aggContent.getJSONObject(agg_operator).put("time_zone", dateTimeZone);
        return this;
    }


    public DateHistogram offset(long offset) {
        aggContent.getJSONObject(agg_operator).put("offset", offset);
        return this;
    }

    public DateHistogram keyOrder(boolean asc) {
        JSONObject order = new JSONObject();
        if (asc) {
            order.put("_key", "asc");
        } else {
            order.put("_key", "desc");
        }
        aggContent.getJSONObject(agg_operator).put("order", order);
        return this;
    }

    @Override
    public DateHistogram subAggregation(Aggregation aggregation) {
        aggContent.put("aggregations", aggregation.getContent());
        return this;
    }

    public static class DateHistogramInterval {

        public static DateHistogramInterval SECOND = new DateHistogramInterval("1s");
        public static DateHistogramInterval MINUTE = new DateHistogramInterval("1m");
        public static DateHistogramInterval HOUR = new DateHistogramInterval("1h");
        public static DateHistogramInterval DAY = new DateHistogramInterval("1d");
        public static DateHistogramInterval WEEK = new DateHistogramInterval("1w");
        public static DateHistogramInterval MONTH = new DateHistogramInterval("1M");
        public static DateHistogramInterval QUARTER = new DateHistogramInterval("1q");
        public static DateHistogramInterval YEAR = new DateHistogramInterval("1y");
        private final String expression;

        DateHistogramInterval(String expression) {
            this.expression = expression;
        }

        public static DateHistogramInterval seconds(int sec) {
            return new DateHistogramInterval(sec + "s");
        }

        public static DateHistogramInterval minutes(int min) {
            return new DateHistogramInterval(min + "m");
        }

        public static DateHistogramInterval hours(int hours) {
            return new DateHistogramInterval(hours + "h");
        }

        public static DateHistogramInterval days(int days) {
            return new DateHistogramInterval(days + "d");
        }

        public static DateHistogramInterval weeks(int weeks) {
            return new DateHistogramInterval(weeks + "w");
        }

        @Override
        public String toString() {
            return expression;
        }
    }

}
