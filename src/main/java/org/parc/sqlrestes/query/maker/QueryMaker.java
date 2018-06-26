package org.parc.sqlrestes.query.maker;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.parc.sqlrestes.domain.Condition;
import org.parc.sqlrestes.domain.Where;
import org.parc.sqlrestes.exception.SqlParseException;

public class QueryMaker extends Maker {

	/**
	 * 将where条件构建成query
	 * 
	 * @param where
	 * @return
	 * @throws SqlParseException
	 */
	public static BoolQueryBuilder explan(Where where) throws SqlParseException {
		return explan(where,true);
	}

    public static BoolQueryBuilder explan(Where where,boolean isQuery) throws SqlParseException {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        while (where.getWheres().size() == 1) {
            where = where.getWheres().getFirst();
        }
        new QueryMaker().explanWhere(boolQuery, where);
        if(isQuery){
            return boolQuery;
        }
        return QueryBuilders.boolQuery().filter(boolQuery);
    }

	private QueryMaker() {
		super(true);
	}

	private void explanWhere(BoolQueryBuilder boolQuery, Where where) throws SqlParseException {
		if (where instanceof Condition) {
			addSubQuery(boolQuery, where, (QueryBuilder) make((Condition) where));
		} else {
			BoolQueryBuilder subQuery = QueryBuilders.boolQuery();
			addSubQuery(boolQuery, where, subQuery);
			for (Where subWhere : where.getWheres()) {
				explanWhere(subQuery, subWhere);
			}
		}
	}

	/**
	 * 增加嵌套插
	 * 
	 * @param boolQuery
	 * @param where
	 * @param subQuery
	 */
	private void addSubQuery(BoolQueryBuilder boolQuery, Where where, QueryBuilder subQuery) {
        if(where instanceof Condition){
            Condition condition = (Condition) where;

            if (condition.isNested()) {
                // bugfix #628
                if ("missing".equalsIgnoreCase(String.valueOf(condition.getValue())) && (condition.getOpear() == Condition.OPEAR.IS || condition.getOpear() == Condition.OPEAR.EQ)) {
                    boolQuery.mustNot(QueryBuilders.nestedQuery(condition.getNestedPath(), QueryBuilders.boolQuery().mustNot(subQuery), ScoreMode.None));
                    return;
                }

                subQuery = QueryBuilders.nestedQuery(condition.getNestedPath(), subQuery, ScoreMode.None);
            } else if(condition.isChildren()) {

//            	subQuery = JoinQueryBuilders.hasChildQuery(condition.getChildType(), subQuery, ScoreMode.None);
            }
        }

		if (where.getConn() == Where.CONN.AND) {
			boolQuery.must(subQuery);
		} else {
			boolQuery.should(subQuery);
		}
	}
}
