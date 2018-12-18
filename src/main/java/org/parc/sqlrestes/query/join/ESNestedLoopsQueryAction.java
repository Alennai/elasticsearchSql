package org.parc.sqlrestes.query.join;


import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.domain.JoinSelect;
import org.parc.sqlrestes.domain.Where;
import org.parc.sqlrestes.domain.hints.Hint;
import org.parc.sqlrestes.domain.hints.HintType;

/**
 * Created by Eliran on 15/9/2015.
 */
public class ESNestedLoopsQueryAction extends ESJoinQueryAction {

    public ESNestedLoopsQueryAction(RestClient client, JoinSelect joinSelect) {
        super(client, joinSelect);
    }

    @Override
    protected void fillSpecificRequestBuilder(JoinRequestBuilder requestBuilder) {
        NestedLoopsElasticRequestBuilder nestedBuilder = (NestedLoopsElasticRequestBuilder) requestBuilder;
        Where where = joinSelect.getConnectedWhere();
        nestedBuilder.setConnectedWhere(where);

    }

    @Override
    protected JoinRequestBuilder createSpecificBuilder() {
        return new NestedLoopsElasticRequestBuilder();
    }

    @Override
    protected void updateRequestWithHints(JoinRequestBuilder requestBuilder) {
        super.updateRequestWithHints(requestBuilder);
        for(Hint hint : this.joinSelect.getHints()){
            if(hint.getType() ==  HintType.NL_MULTISEARCH_SIZE){
                Integer multiSearchMaxSize = (Integer) hint.getParams()[0];
                ((NestedLoopsElasticRequestBuilder) requestBuilder).setMultiSearchMaxSize(multiSearchMaxSize);
            }
        }
    }

    private String removeAlias(String field) {
        String alias = joinSelect.getFirstTable().getAlias();
        if(!field.startsWith(alias+"."))
            alias = joinSelect.getSecondTable().getAlias();
        return field.replace(alias+".","");
    }

}
