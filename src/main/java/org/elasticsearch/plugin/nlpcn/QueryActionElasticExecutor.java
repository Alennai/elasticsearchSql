package org.elasticsearch.plugin.nlpcn;

import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.*;
import org.parc.sqlrestes.query.join.ESJoinQueryAction;
import org.parc.sqlrestes.query.multi.MultiQueryAction;
import org.parc.sqlrestes.query.multi.MultiQueryRequestBuilder;

import java.io.IOException;

/**
 * Created by Eliran on 3/10/2015.
 */
public class QueryActionElasticExecutor {
    public static SearchHits executeSearchAction(DefaultQueryAction searchQueryAction) throws SqlParseException {
        SqlElasticSearchRequestBuilder builder  =  searchQueryAction.explain();
        return ((SearchResponse) builder.get()).getHits();
    }

    public static SearchHits executeJoinSearchAction(RestClient client , ESJoinQueryAction joinQueryAction) throws IOException, SqlParseException {
        SqlElasticRequestBuilder joinRequestBuilder = joinQueryAction.explain();
        ElasticJoinExecutor executor = ElasticJoinExecutor.createJoinExecutor(client,joinRequestBuilder);
        executor.run();
        return executor.getHits();
    }

    public static Aggregations executeAggregationAction(AggregationQueryAction aggregationQueryAction) throws SqlParseException {
        SqlElasticSearchRequestBuilder select =  aggregationQueryAction.explain();
        return ((SearchResponse)select.get()).getAggregations();
    }

    public static ActionResponse executeDeleteAction(DeleteQueryAction deleteQueryAction) throws SqlParseException {
        return deleteQueryAction.explain().get();
    }

    public static SearchHits executeMultiQueryAction(RestClient client, MultiQueryAction queryAction) throws SqlParseException, IOException {
        SqlElasticRequestBuilder multiRequestBuilder = queryAction.explain();
        ElasticHitsExecutor executor = MultiRequestExecutorFactory.createExecutor(client, (MultiQueryRequestBuilder) multiRequestBuilder);
        executor.run();
        return executor.getHits();
    }

    public static Object executeAnyAction(RestClient client , QueryAction queryAction) throws SqlParseException, IOException {
        if(queryAction instanceof DefaultQueryAction)
            return executeSearchAction((DefaultQueryAction) queryAction);
        if(queryAction instanceof AggregationQueryAction)
            return executeAggregationAction((AggregationQueryAction) queryAction);
        if(queryAction instanceof ESJoinQueryAction)
            return executeJoinSearchAction(client, (ESJoinQueryAction) queryAction);
        if(queryAction instanceof MultiQueryAction)
            return executeMultiQueryAction(client, (MultiQueryAction) queryAction);
        if(queryAction instanceof DeleteQueryAction )
            return executeDeleteAction((DeleteQueryAction) queryAction);
        return null;
    }


}
