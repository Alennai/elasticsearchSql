package org.parc.sqlrestes.query.join;

import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.domain.Field;
import org.parc.sqlrestes.domain.JoinSelect;
import org.parc.sqlrestes.domain.Select;
import org.parc.sqlrestes.domain.TableOnJoinSelect;
import org.parc.sqlrestes.domain.hints.Hint;
import org.parc.sqlrestes.domain.hints.HintType;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.DefaultQueryAction;
import org.parc.sqlrestes.query.QueryAction;
import org.parc.sqlrestes.query.SqlElasticRequestBuilder;

import java.util.List;


/**
 * Created by Eliran on 15/9/2015.
 */
public abstract class ESJoinQueryAction extends QueryAction {

    protected JoinSelect joinSelect;

    public ESJoinQueryAction(RestClient client, JoinSelect joinSelect) {
        super(client, null);
        this.joinSelect = joinSelect;
    }

    @Override
    public SqlElasticRequestBuilder explain() throws SqlParseException {
        JoinRequestBuilder requestBuilder = createSpecificBuilder();
        fillBasicJoinRequestBuilder(requestBuilder);
        fillSpecificRequestBuilder(requestBuilder);
        return requestBuilder;
    }

    protected abstract void fillSpecificRequestBuilder(JoinRequestBuilder requestBuilder) throws SqlParseException;

    protected abstract JoinRequestBuilder createSpecificBuilder();


    private void fillBasicJoinRequestBuilder(JoinRequestBuilder requestBuilder) throws SqlParseException {

        fillTableInJoinRequestBuilder(requestBuilder.getFirstTable(), joinSelect.getFirstTable());
        fillTableInJoinRequestBuilder(requestBuilder.getSecondTable(), joinSelect.getSecondTable());

        requestBuilder.setJoinType(joinSelect.getJoinType());

        requestBuilder.setTotalLimit(joinSelect.getTotalLimit());

        updateRequestWithHints(requestBuilder);


    }

    protected void updateRequestWithHints(JoinRequestBuilder requestBuilder){
        for(Hint hint : joinSelect.getHints()) {
            if (hint.getType() == HintType.JOIN_LIMIT) {
                Object[] params = hint.getParams();
                requestBuilder.getFirstTable().setHintLimit((Integer) params[0]);
                requestBuilder.getSecondTable().setHintLimit((Integer) params[1]);
            }
        }
    }

    private void fillTableInJoinRequestBuilder(TableInJoinRequestBuilder requestBuilder, TableOnJoinSelect tableOnJoinSelect) throws SqlParseException {
        List<Field> connectedFields = tableOnJoinSelect.getConnectedFields();
        addFieldsToSelectIfMissing(tableOnJoinSelect,connectedFields);
        requestBuilder.setOriginalSelect(tableOnJoinSelect);
        DefaultQueryAction queryAction = new DefaultQueryAction(client,tableOnJoinSelect);
        queryAction.explain();
        requestBuilder.setRequestBuilder(queryAction.getRequestBuilder());
        requestBuilder.setReturnedFields(tableOnJoinSelect.getSelectedFields());
        requestBuilder.setAlias(tableOnJoinSelect.getAlias());
    }

    private void addFieldsToSelectIfMissing(Select select, List<Field> fields) {
        //this means all fields
        if(select.getFields() == null || select.getFields().size() == 0) return;

        List<Field> selectedFields = select.getFields();
        for(Field field : fields){
            if(!selectedFields.contains(field)){
                selectedFields.add(field);
            }
        }

    }

}
