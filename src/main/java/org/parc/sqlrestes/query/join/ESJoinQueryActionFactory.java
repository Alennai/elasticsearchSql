package org.parc.sqlrestes.query.join;

import org.elasticsearch.client.RestClient;
import org.parc.sqlrestes.domain.Condition;
import org.parc.sqlrestes.domain.JoinSelect;
import org.parc.sqlrestes.domain.hints.Hint;
import org.parc.sqlrestes.domain.hints.HintType;
import org.parc.sqlrestes.query.QueryAction;

import java.util.List;

/**
 * Created by Eliran on 15/9/2015.
 */
public class ESJoinQueryActionFactory {
    public static QueryAction createJoinAction(RestClient client, JoinSelect joinSelect) {
        List<Condition> connectedConditions = joinSelect.getConnectedConditions();
        boolean allEqual = true;
        for (Condition condition : connectedConditions) {
            if (condition.getOpear() != Condition.OPEAR.EQ) {
                allEqual = false;
                break;
            }

        }
        if (!allEqual)
            return new ESNestedLoopsQueryAction(client, joinSelect);

        boolean useNestedLoopsHintExist = false;
        for (Hint hint : joinSelect.getHints()) {
            if (hint.getType() == HintType.USE_NESTED_LOOPS) {
                useNestedLoopsHintExist = true;
                break;
            }
        }
        if (useNestedLoopsHintExist)
            return new ESNestedLoopsQueryAction(client, joinSelect);

        return new ESHashJoinQueryAction(client, joinSelect);

    }
}
