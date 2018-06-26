package org.parc.sqlrestes.query;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequestBuilder;
import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.exception.SqlParseException;

/**
 * Created by Eliran on 6/10/2015.
 */
public class ShowQueryAction extends QueryAction {
    private String sql;
    public ShowQueryAction(RestClient client, String sql) {
        super(client,null);
        this.sql = sql;
    }

    @Override
    public SqlElasticRequestBuilder explain() throws SqlParseException {
        String sql = this.sql.replaceAll("\\s+"," ");
        //todo: support indices with space?
        String indexName = sql.split(" ")[1];
        final GetIndexRequestBuilder indexRequestBuilder ;
        String type = null;
        if(indexName.contains("/")){
            String[] indexAndType = indexName.split("\\/");
            indexName = indexAndType[0];
            type = indexAndType[1];
        }
//        indexRequestBuilder = client.admin().indices().prepareGetIndex();
        indexRequestBuilder =null;
        if(!indexName.equals("*")){
            indexRequestBuilder.addIndices(indexName);
            if(type!=null && !type.equals("")){
                indexRequestBuilder.setTypes(type);
            }
        }
        indexRequestBuilder.addFeatures(GetIndexRequest.Feature.MAPPINGS);

        return new SqlElasticRequestBuilder() {
            @Override
            public ActionRequest request() {
                return indexRequestBuilder.request();
            }

            @Override
            public String explain() {
                return indexRequestBuilder.toString();
            }

            @Override
            public ActionResponse get() {
                return indexRequestBuilder.get();
            }

            @Override
            public ActionRequestBuilder getBuilder() {
                return indexRequestBuilder;
            }
        };
    }
}
