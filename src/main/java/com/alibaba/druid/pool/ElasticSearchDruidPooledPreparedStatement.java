package com.alibaba.druid.pool;

import org.elasticsearch.client.RestClient;
import org.parc.plugin.QueryActionElasticExecutor;
import org.parc.plugin.executors.CsvExtractorException;
import org.parc.sqlrestes.SearchDao;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.jdbc.ObjectResult;
import org.parc.sqlrestes.jdbc.ObjectResultsExtractor;
import org.parc.sqlrestes.query.QueryAction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

public class ElasticSearchDruidPooledPreparedStatement extends DruidPooledPreparedStatement {


    RestClient client = null;

    public ElasticSearchDruidPooledPreparedStatement(DruidPooledConnection conn, PreparedStatementHolder holder) throws SQLException {
        super(conn, holder);
        this.client = ((RestClientConnection) conn.getConnection()).getClient();
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        checkOpen();

        incrementExecuteCount();
        transactionRecord(getSql());

        oracleSetRowPrefetch();

        conn.beforeExecute();
        try {


            ObjectResult extractor = getObjectResult(true, getSql(), false, false, true);
            List<String> headers = extractor.getHeaders();
            List<List<Object>> lines = extractor.getLines();

            ResultSet rs = new ElasticSearchResultSet(this, headers, lines);

            if (rs == null) {
                return null;
            }

            DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            addResultSetTrace(poolableResultSet);

            return poolableResultSet;
        } catch (Throwable t) {
            throw checkException(t);
        } finally {
            conn.afterExecute();
        }
    }

    private ObjectResult getObjectResult(boolean flat, String query, boolean includeScore, boolean includeType, boolean includeId) throws SqlParseException, SQLFeatureNotSupportedException, Exception, CsvExtractorException {
        SearchDao searchDao = new org.parc.sqlrestes.SearchDao(client);

        //String rewriteSQL = searchDao.explain(getSql()).explain().explain();

       QueryAction queryAction = searchDao.explain(query);
        Object execution = QueryActionElasticExecutor.executeAnyAction(searchDao.getClient(), queryAction);
        return new ObjectResultsExtractor(includeScore, includeType, includeId).extractResults(execution, flat);
    }

    @Override
    public int executeUpdate() throws SQLException {
        throw new SQLException("executeUpdate not support in ElasticSearch");
    }
}
