package org.parc.sqlrestes.query;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;

public interface SqlElasticRequestBuilder {
    public ActionRequest request();
    public String explain();
    public ActionResponse get();
    public ActionRequestBuilder getBuilder();

}
