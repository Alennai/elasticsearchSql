package org.elasticsearch.plugin.nlpcn;

import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.SqlElasticDeleteByQueryRequestBuilder;
import org.parc.sqlrestes.query.SqlElasticRequestBuilder;
import org.parc.sqlrestes.query.join.JoinRequestBuilder;

import java.io.IOException;


public class ActionRequestRestExecuter {

	private RestChannel channel;
	private RestClient client;
	private SqlElasticRequestBuilder requestBuilder;

	public ActionRequestRestExecuter(SqlElasticRequestBuilder requestBuilder, RestChannel channel, final RestClient client) {
		this.requestBuilder = requestBuilder;
		this.channel = channel;
		this.client = client;
	}



    /**
	 * Execute the ActionRequest and returns the REST response using the channel.
	 */
	public void execute() throws Exception {
        ActionRequest request = requestBuilder.request();

        //todo: maby change to instanceof multi?
        if(requestBuilder instanceof JoinRequestBuilder){
            executeJoinRequestAndSendResponse();
        }
		else if (request instanceof SearchRequest) {
			client.search((SearchRequest) request, new RestStatusToXContentListener<SearchResponse>(channel));
		} else if (requestBuilder instanceof SqlElasticDeleteByQueryRequestBuilder) {
            throw new UnsupportedOperationException("currently not support delete on elastic 2.0.0");
        }
        else if(request instanceof GetIndexRequest) {
            this.requestBuilder.getBuilder().execute( new GetIndexRequestRestListener(channel, (GetIndexRequest) request));
        }


		else {
			throw new Exception(String.format("Unsupported ActionRequest provided: %s", request.getClass().getName()));
		}
	}

    private void executeJoinRequestAndSendResponse() throws IOException, SqlParseException {
        ElasticJoinExecutor executor = ElasticJoinExecutor.createJoinExecutor(client,requestBuilder);
        executor.run();
        executor.sendResponse(channel);
    }

}
