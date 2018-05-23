package org.parc.sqlrestes.query.join;


import org.parc.restes.RestQueryBuilder;
import org.parc.sqlrestes.domain.Field;
import org.parc.sqlrestes.domain.Select;

import java.util.List;

/**
 * Created by Eliran on 28/8/2015.
 */
public class TableInJoinRequestBuilder {
    private RestQueryBuilder requestBuilder;
    private String alias;
    private List<Field> returnedFields;
    private Select originalSelect;
    private Integer hintLimit;

    public TableInJoinRequestBuilder() {
    }

    public RestQueryBuilder getRequestBuilder() {
        return requestBuilder;
    }

    public void setRequestBuilder(RestQueryBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Field> getReturnedFields() {
        return returnedFields;
    }

    public void setReturnedFields(List<Field> returnedFields) {
        this.returnedFields = returnedFields;
    }

    public Select getOriginalSelect() {
        return originalSelect;
    }

    public void setOriginalSelect(Select originalSelect) {
        this.originalSelect = originalSelect;
    }

    public Integer getHintLimit() {
        return hintLimit;
    }

    public void setHintLimit(Integer hintLimit) {
        this.hintLimit = hintLimit;
    }
}
