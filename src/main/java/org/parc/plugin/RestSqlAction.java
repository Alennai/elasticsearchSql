package org.parc.plugin;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;
import org.parc.plugin.executors.ActionRequestRestExecuterFactory;
import org.parc.plugin.executors.RestExecutor;
import org.parc.sqlrestes.SearchDao;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.QueryAction;

import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


class RestSqlAction extends BaseRestHandler {

//    public static final RestSqlAction INSTANCE = new RestSqlAction();


    public RestSqlAction(Settings settings, RestController restController) {
        super(settings);
        restController.registerHandler(RestRequest.Method.POST, "/_sql/_explain", this);
        restController.registerHandler(RestRequest.Method.GET, "/_sql/_explain", this);
        restController.registerHandler(RestRequest.Method.POST, "/_sql", this);
        restController.registerHandler(RestRequest.Method.GET, "/_sql", this);
    }

    @Override
    public String getName() {
        return "sql_action";
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) {
        String sql = request.param("sql");

        if (sql == null) {
            sql = request.content().utf8ToString();
        }
        try {
//        SearchDao searchDao = new SearchDao(client);
            SearchDao searchDao = null;
            QueryAction queryAction = null;

            queryAction = searchDao.explain(sql);

            // TODO add unittests to explain. (rest level?)
            if (request.path().endsWith("/_explain")) {
                final String jsonExplanation = queryAction.explain().explain();
                return channel -> channel.sendResponse(new BytesRestResponse(RestStatus.OK, jsonExplanation));
            } else {
                Map<String, String> params = request.params();
                RestExecutor restExecutor = ActionRequestRestExecuterFactory.createExecutor(params.get("format"));
                final QueryAction finalQueryAction = queryAction;
                //doing this hack because elasticsearch throws exception for un-consumed props
                Map<String, String> additionalParams = new HashMap<>();
                for (String paramName : responseParams()) {
                    if (request.hasParam(paramName)) {
                        additionalParams.put(paramName, request.param(paramName));
                    }
                }
                return channel -> restExecutor.execute(client, additionalParams, finalQueryAction, channel);
            }
        } catch (SqlParseException | SQLFeatureNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Set<String> responseParams() {
        Set<String> responseParams = new HashSet<>(super.responseParams());
        responseParams.addAll(Arrays.asList("sql", "flat", "separator", "_score", "_type", "_id", "newLine", "format"));
        return responseParams;
    }
}