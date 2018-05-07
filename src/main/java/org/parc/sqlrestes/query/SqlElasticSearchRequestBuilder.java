package org.parc.sqlrestes.query;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;

/**
 * Created by Eliran on 19/8/2015.
 */
public class SqlElasticSearchRequestBuilder implements SqlElasticRequestBuilder {
    SearchRequestBuilder requestBuilder;

    public SqlElasticSearchRequestBuilder(SearchRequestBuilder requestBuilder) {
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
