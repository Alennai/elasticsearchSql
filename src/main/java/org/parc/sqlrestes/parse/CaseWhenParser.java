package org.parc.sqlrestes.parse;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.google.common.base.Joiner;
import org.parc.sqlrestes.Util;
import org.parc.sqlrestes.domain.Condition;
import org.parc.sqlrestes.domain.Where;
import org.parc.sqlrestes.exception.SqlParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allwefantasy on 9/3/16.
 */
class CaseWhenParser {
    private SQLCaseExpr caseExpr;

    public CaseWhenParser(SQLCaseExpr caseExpr, String alias, String tableAlias) {
        String alias1 = alias;
        String tableAlias1 = tableAlias;
        this.caseExpr = caseExpr;

    }

    public String parse() throws SqlParseException {
        List<String> result = new ArrayList<String>();

        for (SQLCaseExpr.Item item : caseExpr.getItems()) {
            SQLExpr conditionExpr = item.getConditionExpr();

            WhereParser parser = new WhereParser(new SqlParser(), conditionExpr);
            String scriptCode = explain(parser.findWhere());
            if (scriptCode.startsWith(" &&")) {
                scriptCode = scriptCode.substring(3);
            }
            if (result.size() == 0) {
                result.add("if(" + scriptCode + ")" + "{" + Util.getScriptValueWithQuote(item.getValueExpr(), "'") + "}");
            } else {
                result.add("else if(" + scriptCode + ")" + "{" + Util.getScriptValueWithQuote(item.getValueExpr(), "'") + "}");
            }

        }
        SQLExpr elseExpr = caseExpr.getElseExpr();
        if (elseExpr == null) {
            result.add("else { null }");
        } else {
            result.add("else {" + Util.getScriptValueWithQuote(elseExpr, "'") + "}");
        }


        return Joiner.on(" ").join(result);
    }

    private String explain(Where where) throws SqlParseException {
        List<String> codes = new ArrayList<String>();
        while (where.getWheres().size() == 1) {
            where = where.getWheres().getFirst();
        }
        explainWhere(codes, where);
        String relation = where.getConn().name().equals("AND") ? " && " : " || ";
        return Joiner.on(relation).join(codes);
    }


    private void explainWhere(List<String> codes, Where where) throws SqlParseException {
        if (where instanceof Condition) {
            Condition condition = (Condition) where;
            String relation = condition.getConn().name().equals("AND") ? " && " : " || ";
            if (condition.getValue() instanceof ScriptFilter) {
                codes.add(relation + "(" + ((ScriptFilter) condition.getValue()).getScript() + ")");
            } else {
                codes.add(relation + "(" + Util.getScriptValueWithQuote(condition.getNameExpr(), "'") + condition.getOpertatorSymbol() + Util.getScriptValueWithQuote(condition.getValueExpr(), "'") + ")");
            }
        } else {
            for (Where subWhere : where.getWheres()) {
                List<String> subCodes = new ArrayList<String>();
                explainWhere(subCodes, subWhere);
                String relation = subWhere.getConn().name().equals("AND") ? "&&" : "||";
                codes.add("(" + Joiner.on(relation).join(subCodes) + ")");
            }
        }
    }
}
