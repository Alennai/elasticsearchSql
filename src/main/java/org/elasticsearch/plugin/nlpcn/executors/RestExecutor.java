package org.elasticsearch.plugin.nlpcn.executors;

import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.query.QueryAction;

import java.util.Map;

/**
 * Created by Eliran on 26/12/2015.
 */
public interface RestExecutor {
    public void execute(RestClient client, Map<String, String> params, QueryAction queryAction, RestChannel channel) throws Exception;

    public String execute(RestClient client, Map<String, String> params, QueryAction queryAction) throws Exception;
}
