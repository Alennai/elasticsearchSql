package org.parc.sqlrestes;

import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.ESActionFactory;
import org.parc.sqlrestes.query.QueryAction;

import java.sql.SQLFeatureNotSupportedException;
import java.util.HashSet;
import java.util.Set;


public class SearchDao {

    private static final Set<String> END_TABLE_MAP = new HashSet<>();

    static {
        END_TABLE_MAP.add("limit");
        END_TABLE_MAP.add("order");
        END_TABLE_MAP.add("where");
        END_TABLE_MAP.add("group");

    }

    private RestClient client = null;

    //client 传来传去是为了解析sql。
    public SearchDao(RestClient client) {
        this.client = client;
    }

    public RestClient getClient() {
        return client;
    }

    /**
     * Prepare action And transform sql
     * into ES ActionRequest
     *
     * @param sql SQL query to execute.
     * @return ES request
     * @throws SqlParseException
     */
    public QueryAction explain(String sql) throws SqlParseException, SQLFeatureNotSupportedException {
        return ESActionFactory.create(client, sql);
    }


}
