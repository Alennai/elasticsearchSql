package com.alibaba.druid.pool;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Created by xusiao on 2018/5/2.
 */
class RestClientConnection implements Connection {

    private RestClient client;

    public RestClientConnection(String jdbcUrl) {
        String hostAndPortArrayStr = jdbcUrl.split("/")[2];
        String[] hostAndPortArray = hostAndPortArrayStr.split(",");
        HttpHost[] hosts = new HttpHost[hostAndPortArray.length];
        for ( int i=0;i<hostAndPortArray.length;i++) {
            String hostAndPort=hostAndPortArray[i];
            String host = hostAndPort.split(":")[i];
            String port = hostAndPort.split(":")[i];
            hosts[i] = new HttpHost(host, Integer.parseInt(port), "http");
        }
         client = RestClient.builder(hosts).setMaxRetryTimeoutMillis(10000).build();
    }

    public RestClient getClient() {
        return client;
    }

    public Statement createStatement() {
        return null;
    }

    public PreparedStatement prepareStatement(String sql) {
        return new PreparedStatement() {
            @Override
            public ResultSet executeQuery() {
                return null;
            }

            @Override
            public int executeUpdate() {
                return 0;
            }

            @Override
            public void setNull(int parameterIndex, int sqlType) {

            }

            @Override
            public void setBoolean(int parameterIndex, boolean x) {

            }

            @Override
            public void setByte(int parameterIndex, byte x) {

            }

            @Override
            public void setShort(int parameterIndex, short x) {

            }

            @Override
            public void setInt(int parameterIndex, int x) {

            }

            @Override
            public void setLong(int parameterIndex, long x) {

            }

            @Override
            public void setFloat(int parameterIndex, float x) {

            }

            @Override
            public void setDouble(int parameterIndex, double x) {

            }

            @Override
            public void setBigDecimal(int parameterIndex, BigDecimal x) {

            }

            @Override
            public void setString(int parameterIndex, String x) {

            }

            @Override
            public void setBytes(int parameterIndex, byte[] x) {

            }

            @Override
            public void setDate(int parameterIndex, Date x) {

            }

            @Override
            public void setTime(int parameterIndex, Time x) {

            }

            @Override
            public void setTimestamp(int parameterIndex, Timestamp x) {

            }

            @Override
            public void setAsciiStream(int parameterIndex, InputStream x, int length) {

            }

            @Override
            public void setUnicodeStream(int parameterIndex, InputStream x, int length) {

            }

            @Override
            public void setBinaryStream(int parameterIndex, InputStream x, int length) {

            }

            @Override
            public void clearParameters() {

            }

            @Override
            public void setObject(int parameterIndex, Object x, int targetSqlType) {

            }

            @Override
            public void setObject(int parameterIndex, Object x) {

            }

            @Override
            public boolean execute() {
                return false;
            }

            @Override
            public void addBatch() {

            }

            @Override
            public void setCharacterStream(int parameterIndex, Reader reader, int length) {

            }

            @Override
            public void setRef(int parameterIndex, Ref x) {

            }

            @Override
            public void setBlob(int parameterIndex, Blob x) {

            }

            @Override
            public void setClob(int parameterIndex, Clob x) {

            }

            @Override
            public void setArray(int parameterIndex, Array x) {

            }

            @Override
            public ResultSetMetaData getMetaData() {
                return null;
            }

            @Override
            public void setDate(int parameterIndex, Date x, Calendar cal) {

            }

            @Override
            public void setTime(int parameterIndex, Time x, Calendar cal) {

            }

            @Override
            public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {

            }

            @Override
            public void setNull(int parameterIndex, int sqlType, String typeName) {

            }

            @Override
            public void setURL(int parameterIndex, URL x) {

            }

            @Override
            public ParameterMetaData getParameterMetaData() {
                return null;
            }

            @Override
            public void setRowId(int parameterIndex, RowId x) {

            }

            @Override
            public void setNString(int parameterIndex, String value) {

            }

            @Override
            public void setNCharacterStream(int parameterIndex, Reader value, long length) {

            }

            @Override
            public void setNClob(int parameterIndex, NClob value) {

            }

            @Override
            public void setClob(int parameterIndex, Reader reader, long length) {

            }

            @Override
            public void setBlob(int parameterIndex, InputStream inputStream, long length) {

            }

            @Override
            public void setNClob(int parameterIndex, Reader reader, long length) {

            }

            @Override
            public void setSQLXML(int parameterIndex, SQLXML xmlObject) {

            }

            @Override
            public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {

            }

            @Override
            public void setAsciiStream(int parameterIndex, InputStream x, long length) {

            }

            @Override
            public void setBinaryStream(int parameterIndex, InputStream x, long length) {

            }

            @Override
            public void setCharacterStream(int parameterIndex, Reader reader, long length) {

            }

            @Override
            public void setAsciiStream(int parameterIndex, InputStream x) {

            }

            @Override
            public void setBinaryStream(int parameterIndex, InputStream x) {

            }

            @Override
            public void setCharacterStream(int parameterIndex, Reader reader) {

            }

            @Override
            public void setNCharacterStream(int parameterIndex, Reader value) {

            }

            @Override
            public void setClob(int parameterIndex, Reader reader) {

            }

            @Override
            public void setBlob(int parameterIndex, InputStream inputStream) {

            }

            @Override
            public void setNClob(int parameterIndex, Reader reader) {

            }

            @Override
            public ResultSet executeQuery(String sql) {
                return null;
            }

            @Override
            public int executeUpdate(String sql) {
                return 0;
            }

            @Override
            public void close() {
            }

            @Override
            public int getMaxFieldSize() {
                return 0;
            }

            @Override
            public void setMaxFieldSize(int max) {

            }

            @Override
            public int getMaxRows() {
                return 0;
            }

            @Override
            public void setMaxRows(int max) {

            }

            @Override
            public void setEscapeProcessing(boolean enable) {

            }

            @Override
            public int getQueryTimeout() {
                return 0;
            }

            @Override
            public void setQueryTimeout(int seconds) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public SQLWarning getWarnings() {
                return null;
            }

            @Override
            public void clearWarnings() {

            }

            @Override
            public void setCursorName(String name) {

            }

            @Override
            public boolean execute(String sql) {
                return false;
            }

            @Override
            public ResultSet getResultSet() {
                return null;
            }

            @Override
            public int getUpdateCount() {
                return 0;
            }

            @Override
            public boolean getMoreResults() {
                return false;
            }

            @Override
            public void setFetchDirection(int direction) {

            }

            @Override
            public int getFetchDirection() {
                return 0;
            }

            @Override
            public void setFetchSize(int rows) {

            }

            @Override
            public int getFetchSize() {
                return 0;
            }

            @Override
            public int getResultSetConcurrency() {
                return 0;
            }

            @Override
            public int getResultSetType() {
                return 0;
            }

            @Override
            public void addBatch(String sql) {

            }

            @Override
            public void clearBatch() {

            }

            @Override
            public int[] executeBatch() {
                return new int[0];
            }

            @Override
            public Connection getConnection() {
                return null;
            }

            @Override
            public boolean getMoreResults(int current) {
                return false;
            }

            @Override
            public ResultSet getGeneratedKeys() {
                return null;
            }

            @Override
            public int executeUpdate(String sql, int autoGeneratedKeys) {
                return 0;
            }

            @Override
            public int executeUpdate(String sql, int[] columnIndexes) {
                return 0;
            }

            @Override
            public int executeUpdate(String sql, String[] columnNames) {
                return 0;
            }

            @Override
            public boolean execute(String sql, int autoGeneratedKeys) {
                return false;
            }

            @Override
            public boolean execute(String sql, int[] columnIndexes) {
                return false;
            }

            @Override
            public boolean execute(String sql, String[] columnNames) {
                return false;
            }

            @Override
            public int getResultSetHoldability() {
                return 0;
            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void setPoolable(boolean poolable) {

            }

            @Override
            public boolean isPoolable() {
                return false;
            }

            @Override
            public void closeOnCompletion() {

            }

            @Override
            public boolean isCloseOnCompletion() {
                return false;
            }

            @Override
            public <T> T unwrap(Class<T> iface) {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) {
                return false;
            }
        };
    }

    public CallableStatement prepareCall(String sql) {
        return null;
    }

    public String nativeSQL(String sql) {
        return null;
    }

    public void setAutoCommit(boolean autoCommit) {

    }

    public boolean getAutoCommit() {
        return false;
    }

    public void commit() {

    }

    public void rollback() {

    }

    public void close() {

    }

    public boolean isClosed() {
        return false;
    }

    public DatabaseMetaData getMetaData() {
        return null;
    }

    public void setReadOnly(boolean readOnly) {

    }

    public boolean isReadOnly() {
        return false;
    }

    public void setCatalog(String catalog) {

    }

    public String getCatalog() {
        return null;
    }

    public void setTransactionIsolation(int level) {

    }

    public int getTransactionIsolation() {
        return 0;
    }

    public SQLWarning getWarnings() {
        return null;
    }

    public void clearWarnings() {

    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) {
        return null;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    public Map<String, Class<?>> getTypeMap() {
        return null;
    }

    public void setTypeMap(Map<String, Class<?>> map) {

    }

    public void setHoldability(int holdability) {

    }

    public int getHoldability() {
        return 0;
    }

    public Savepoint setSavepoint() {
        return null;
    }

    public Savepoint setSavepoint(String name) {
        return null;
    }

    public void rollback(Savepoint savepoint) {

    }

    public void releaseSavepoint(Savepoint savepoint) {

    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) {
        return null;
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) {
        return null;
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) {
        return null;
    }

    public Clob createClob() {
        return null;
    }

    public Blob createBlob() {
        return null;
    }

    public NClob createNClob() {
        return null;
    }

    public SQLXML createSQLXML() {
        return null;
    }

    public boolean isValid(int timeout) {
        return false;
    }

    public void setClientInfo(String name, String value) {

    }

    public void setClientInfo(Properties properties) {

    }

    public String getClientInfo(String name) {
        return null;
    }

    public Properties getClientInfo() {
        return null;
    }

    public Array createArrayOf(String typeName, Object[] elements) {
        return null;
    }

    public Struct createStruct(String typeName, Object[] attributes) {
        return null;
    }

    public void setSchema(String schema) {

    }

    public String getSchema() {
        return null;
    }

    public void abort(Executor executor) {

    }

    public void setNetworkTimeout(Executor executor, int milliseconds) {

    }

    public int getNetworkTimeout() {
        return 0;
    }

    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
