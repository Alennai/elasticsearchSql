package org.parc.sqlrestes.query.multi;

import org.elasticsearch.client.RestClient;
import org.parc.restes.RestQueryBuilder;
import org.parc.sqlrestes.domain.Field;
import org.parc.sqlrestes.domain.Select;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.DefaultQueryAction;
import org.parc.sqlrestes.query.QueryAction;
import org.parc.sqlrestes.query.SqlElasticRequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Eliran on 19/8/2016.
 */
public class MultiQueryAction extends QueryAction {
    private MultiQuerySelect multiQuerySelect;
    public MultiQueryAction(RestClient client, MultiQuerySelect multiSelect) {
        super(client, null);
        this.multiQuerySelect = multiSelect;
    }

    @Override
    public SqlElasticRequestBuilder explain() throws SqlParseException {
        if(!isValidMultiSelectReturnFields()){
            throw new SqlParseException("on multi query fields/aliases of one table should be subset of other");
        }
        MultiQueryRequestBuilder requestBuilder = new MultiQueryRequestBuilder(this.multiQuerySelect);
//        requestBuilder.setFirstSearchRequest(createRequestBuilder(this.multiQuerySelect.getFirstSelect()));
//        requestBuilder.setSecondSearchRequest(createRequestBuilder(this.multiQuerySelect.getSecondSelect()));
        requestBuilder.fillTableAliases(this.multiQuerySelect.getFirstSelect().getFields(),this.multiQuerySelect.getSecondSelect().getFields());

        return requestBuilder;
    }

    private boolean isValidMultiSelectReturnFields() {
        List<Field> firstQueryFields = multiQuerySelect.getFirstSelect().getFields();
        List<Field> secondQueryFields = multiQuerySelect.getSecondSelect().getFields();
        if(firstQueryFields.size() > secondQueryFields.size()){
            return isSubsetFields(firstQueryFields, secondQueryFields);
        }
        return isSubsetFields(secondQueryFields, firstQueryFields);
    }

    private boolean isSubsetFields(List<Field> bigGroup, List<Field> smallerGroup) {
        Set<String> biggerGroup = new HashSet<>();
        for(Field field : bigGroup){
            String fieldName = getNameOrAlias(field);
            biggerGroup.add(fieldName);
        }
        for(Field field : smallerGroup){
            String fieldName = getNameOrAlias(field);
            if(!biggerGroup.contains(fieldName)){
                return false;
            }
        }
        return true;
    }

    private String getNameOrAlias(Field field) {
        String fieldName = field.getName();
        if(field.getAlias() != null && !field.getAlias().isEmpty()){
            fieldName = field.getAlias();
        }
        return fieldName;
    }

    protected RestQueryBuilder createRequestBuilder(Select select) throws SqlParseException {
        DefaultQueryAction queryAction = new DefaultQueryAction(client,select);
        queryAction.explain();
        return queryAction.getRequestBuilder();
    }
}
