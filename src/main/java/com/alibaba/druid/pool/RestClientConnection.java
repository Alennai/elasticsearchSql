package com.alibaba.druid.pool;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
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
        for (int i = 0; i < hostAndPortArray.length; i++) {
            String hostAndPort = hostAndPortArray[i];
            String host = hostAndPort.split(":")[i];
            String port = hostAndPort.split(":")[i];
            hosts[i] = new HttpHost(host, Integer.parseInt(port), "http");
        }
        client = RestClient.builder(hosts).setMaxRetryTimeoutMillis(10000).build();
    }

    public RestClient getClient() {
        return client;
    }

    @Override
    public Statement createStatement() {
        return null;
    }

    @Override
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
            public int getFetchDirection() {
                return 0;
            }

            @Override
            public void setFetchDirection(int direction) {

            }

            @Override
            public int getFetchSize() {
                return 0;
            }

            @Override
            public void setFetchSize(int rows) {

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
            public boolean isPoolable() {
                return false;
            }

            @Override
            public void setPoolable(boolean poolable) {

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

    @Override
    public CallableStatement prepareCall(String sql) {
        return null;
    }

    @Override
    public String nativeSQL(String sql) {
        return null;
    }

    @Override
    public boolean getAutoCommit() {
        return false;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public String getCatalog() {
        return null;
    }

    @Override
    public void setCatalog(String catalog) {

    }

    @Override
    public int getTransactionIsolation() {
        return 0;
    }

    @Override
    public void setTransactionIsolation(int level) {

    }

    @Override
    public SQLWarning getWarnings() {
        return null;
    }

    @Override
    public void clearWarnings() {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() {
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) {

    }

    @Override
    public int getHoldability() {
        return 0;
    }

    @Override
    public void setHoldability(int holdability) {

    }

    @Override
    public Savepoint setSavepoint() {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) {

    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) {

    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) {
        return null;
    }

    @Override
    public Clob createClob() {
        return null;
    }

    @Override
    public Blob createBlob() {
        return null;
    }

    @Override
    public NClob createNClob() {
        return null;
    }

    @Override
    public SQLXML createSQLXML() {
        return null;
    }

    @Override
    public boolean isValid(int timeout) {
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) {

    }

    @Override
    public String getClientInfo(String name) {
        return null;
    }

    @Override
    public Properties getClientInfo() {
        return null;
    }

    @Override
    public void setClientInfo(Properties properties) {

    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) {
        return null;
    }

    @Override
    public String getSchema() {
        return null;
    }

    @Override
    public void setSchema(String schema) {

    }

    @Override
    public void abort(Executor executor) {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) {

    }

    @Override
    public int getNetworkTimeout() {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
