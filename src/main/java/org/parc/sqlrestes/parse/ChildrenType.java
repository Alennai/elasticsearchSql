package org.parc.sqlrestes.parse;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLTextLiteralExpr;
import org.parc.sqlrestes.Util;
import org.parc.sqlrestes.domain.Where;
import org.parc.sqlrestes.exception.SqlParseException;

import java.util.List;

/**
 * Created by Razma Tazz on 14/04/2016.
 */
public class ChildrenType {
    public String field;
    public String childType;
    public Where where;
    private boolean simple;

    public boolean tryFillFromExpr(SQLExpr expr) throws SqlParseException {
        if (!(expr instanceof SQLMethodInvokeExpr)) return false;
        SQLMethodInvokeExpr method = (SQLMethodInvokeExpr) expr;

        String methodName = method.getMethodName();

        if (!methodName.toLowerCase().equals("children")) return false;

        List<SQLExpr> parameters = method.getParameters();

        if (parameters.size() != 2)
            throw new SqlParseException("on children object only allowed 2 parameters (type, field)/(type, conditions...) ");

        this.childType = Util.extendedToString(parameters.get(0));
        
        SQLExpr secondParameter = parameters.get(1);
        if(secondParameter instanceof SQLTextLiteralExpr || secondParameter instanceof SQLIdentifierExpr || secondParameter instanceof SQLPropertyExpr) {
            this.field = Util.extendedToString(secondParameter);
            this.simple = true;
        } else {
            Where where = Where.newInstance();
            new WhereParser(new SqlParser()).parseWhere(secondParameter,where);
            if(where.getWheres().size() == 0)
                throw new SqlParseException("unable to parse filter where.");
            this.where = where;
            simple = false;
        }
        
        return true;
    }

    public boolean isSimple() {
        return simple;
    }
}
