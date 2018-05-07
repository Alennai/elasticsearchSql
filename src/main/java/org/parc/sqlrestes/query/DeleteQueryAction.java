package org.parc.sqlrestes.query;


import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.domain.Delete;
import org.parc.sqlrestes.domain.Where;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.maker.QueryMaker;

public class DeleteQueryAction extends QueryAction {

	private final Delete delete;
	private DeleteByQueryRequestBuilder request;

	public DeleteQueryAction(RestClient client, Delete delete) {
		super(client, delete);
		this.delete = delete;
	}

	@Override
	public SqlElasticDeleteByQueryRequestBuilder explain() throws SqlParseException {
		this.request = new DeleteByQueryRequestBuilder(client, DeleteByQueryAction.INSTANCE);

		setIndicesAndTypes();
		setWhere(delete.getWhere());
        SqlElasticDeleteByQueryRequestBuilder deleteByQueryRequestBuilder = new SqlElasticDeleteByQueryRequestBuilder(request);
		return deleteByQueryRequestBuilder;
	}


	/**
	 * Set indices and types to the delete by query request.
	 */
	private void setIndicesAndTypes() {

        DeleteByQueryRequest innerRequest = request.request();
        innerRequest.indices(query.getIndexArr());
        String[] typeArr = query.getTypeArr();
        if (typeArr!=null){
            innerRequest.getSearchRequest().types(typeArr);
        }
//		String[] typeArr = query.getTypeArr();
//		if (typeArr != null) {
//            request.set(typeArr);
//		}
	}


	/**
	 * Create filters based on
	 * the Where clause.
	 *
	 * @param where the 'WHERE' part of the SQL query.
	 * @throws SqlParseException
	 */
	private void setWhere(Where where) throws SqlParseException {
		if (where != null) {
			QueryBuilder whereQuery = QueryMaker.explan(where);
			request.filter(whereQuery);
		} else {
			request.filter(QueryBuilders.matchAllQuery());
		}
	}

}
