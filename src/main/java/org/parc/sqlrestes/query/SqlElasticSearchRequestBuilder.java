package org.parc.sqlrestes.query;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.parc.restes.RestQueryBuilder;

/**
 * Created by Eliran on 19/8/2015.
 */
public class SqlElasticSearchRequestBuilder implements SqlElasticRequestBuilder {
    RestQueryBuilder requestBuilder;

    public SqlElasticSearchRequestBuilder(RestQueryBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public ActionRequest request() {
        return requestBuilder.request();
    }

    @Override
    public String explain() {
        return requestBuilder.toString();
    }

    @Override
    public ActionResponse get() {
        return requestBuilder.get();
    }

    @Override
    public ActionRequestBuilder getBuilder() {
        return requestBuilder;
    }

    @Override
    public String toString(){
        return this.requestBuilder.toString();
    }
}
