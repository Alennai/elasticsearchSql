package org.parc.plugin.executors;

import org.elasticsearch.client.Client;
import org.elasticsearch.rest.RestChannel;
import org.parc.sqlrestes.query.QueryAction;

import java.util.Map;

/**
 * Created by Eliran on 26/12/2015.
 */
public interface RestExecutor {
    void execute(Client client, Map<String, String> params, QueryAction queryAction, RestChannel channel) throws Exception;

    String execute(Client client, Map<String, String> params, QueryAction queryAction) throws Exception;
}
