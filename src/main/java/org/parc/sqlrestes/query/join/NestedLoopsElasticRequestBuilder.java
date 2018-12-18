package org.parc.sqlrestes.query.join;


import org.parc.sqlrestes.domain.Where;
import org.parc.sqlrestes.entity.QueryBuilder;

/**
 * Created by Eliran on 15/9/2015.
 */
public class NestedLoopsElasticRequestBuilder extends JoinRequestBuilder {

    private Where connectedWhere;
    private int multiSearchMaxSize;

    public NestedLoopsElasticRequestBuilder() {

        multiSearchMaxSize = 100;
    }

    @Override
    public String explain() {
        String baseExplain = super.explain();
        Where where = this.connectedWhere;
        QueryBuilder explan = null;
//        try {
        if (where != null) {
            ;
        }
//                explan = QueryMaker.explan(where,false);
//        } catch (SqlParseException e) {
//        }
        String conditions = explan == null ? "Could not parse conditions" : explan.toString();
        return "Nested Loops \n run first query , and for each result run second query with additional conditions :\n" + conditions + "\n" + baseExplain;
    }


    public int getMultiSearchMaxSize() {
        return multiSearchMaxSize;
    }

    public void setMultiSearchMaxSize(int multiSearchMaxSize) {
        this.multiSearchMaxSize = multiSearchMaxSize;
    }

    public Where getConnectedWhere() {
        return connectedWhere;
    }

    public void setConnectedWhere(Where connectedWhere) {
        this.connectedWhere = connectedWhere;
    }
}
