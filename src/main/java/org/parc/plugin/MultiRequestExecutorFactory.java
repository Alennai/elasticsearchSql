package org.parc.plugin;

import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.multi.MultiQueryRequestBuilder;

/**
 * Created by Eliran on 21/8/2016.
 */
public class MultiRequestExecutorFactory {
    public static ElasticHitsExecutor createExecutor(RestClient client, MultiQueryRequestBuilder builder) throws SqlParseException {
        switch (builder.getRelation()) {
            case UNION_ALL:
            case UNION:
                return new UnionExecutor(client, builder);
            case MINUS:
                return new MinusExecutor(client, builder);
            default:
                throw new SqlParseException("only supports union, and minus operations");
        }
    }
}
