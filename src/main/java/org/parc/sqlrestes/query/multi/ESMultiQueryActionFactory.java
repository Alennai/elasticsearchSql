package org.parc.sqlrestes.query.multi;

import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;import org.elasticsearch.client.Client;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.query.QueryAction;

/**
 * Created by Eliran on 19/8/2016.
 */
public class ESMultiQueryActionFactory {

    public static QueryAction createMultiQueryAction(Client client, MultiQuerySelect multiSelect) throws SqlParseException {
        switch (multiSelect.getOperation()){
            case SQLUnionOperator.UNION_ALL:
            case SQLUnionOperator.UNION:
                return new MultiQueryAction(client,multiSelect);
            default:
                throw new SqlParseException("only supports union and union all");
        }
    }
}
