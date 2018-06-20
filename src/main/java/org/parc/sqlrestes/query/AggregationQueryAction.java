package org.parc.sqlrestes.query;

import com.google.common.collect.Lists;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.nested.ReverseNestedAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.parc.restes.RestQueryBuilder;
import org.parc.restes.query.Aggregation;
import org.parc.restes.query.AggregationFactory;
import org.parc.restes.query.aggregations.TermsAgg;
import org.parc.sqlrestes.domain.*;
import org.parc.sqlrestes.domain.hints.Hint;
import org.parc.sqlrestes.domain.hints.HintType;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.maker.AggMaker;
import org.parc.sqlrestes.query.maker.QueryMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Transform SQL query to Elasticsearch aggregations query
 */
public class AggregationQueryAction extends QueryAction {

    private final Select select;
    private AggMaker aggMaker = new AggMaker();
    private RestQueryBuilder request;

    public AggregationQueryAction(RestClient client, Select select) {
        super(client, select);
        this.select = select;
    }

    @Override
    public SqlElasticSearchRequestBuilder explain() throws SqlParseException {
        this.request = new RestQueryBuilder();

        setIndicesAndTypes();

        setWhere(select.getWhere());
        Aggregation lastAgg = null;

        for (List<Field> groupBy : select.getGroupBys()) {
            if (!groupBy.isEmpty()) {
                Field field = groupBy.get(0);


                //make groupby can reference to field alias
                lastAgg = getGroupAgg(field, select);
/*
                if (lastAgg != null && lastAgg instanceof TermsAggregationBuilder && !(field instanceof MethodField)) {
                    //if limit size is too small, increasing shard  size is required
                    if (select.getRowCount() < 200) {
                        ((Aggregation) lastAgg).shardSize(2000);
                        for (Hint hint : select.getHints()) {
                            if (hint.getType() == HintType.SHARD_SIZE) {
                                if (hint.getParams() != null && hint.getParams().length != 0 && hint.getParams()[0] != null) {
                                    ((Aggregation) lastAgg).shardSize((Integer) hint.getParams()[0]);
                                }
                            }
                        }
                    }
                    if(select.getRowCount()>0) {
                        ((Aggregation) lastAgg).size(select.getRowCount());
                    }
                }
*/
                if (field.isNested()) {
                    Aggregation nestedBuilder = createNestedAggregation(field);

                    if (insertFilterIfExistsAfter(lastAgg, groupBy, nestedBuilder, 1)) {
                        groupBy.remove(1);
                    } else {
                        nestedBuilder.subAggregation(lastAgg);
                    }

                    request.addAggregations(wrapNestedIfNeeded(nestedBuilder, field.isReverseNested()));
                } else if (field.isChildren()) {
                    Aggregation childrenBuilder = createChildrenAggregation(field);

                    if (insertFilterIfExistsAfter(lastAgg, groupBy, childrenBuilder, 1)) {
                        groupBy.remove(1);
                    } else {
                        childrenBuilder.subAggregation(lastAgg);
                    }

                    request.addAggregations(childrenBuilder);
                } else {
                    request.addAggregations(lastAgg);
                }

                for (int i = 1; i < groupBy.size(); i++) {
                    field = groupBy.get(i);
                    Aggregation subAgg = getGroupAgg(field, select);
                      //ES5.0 termsaggregation with size = 0 not supported anymore
//                    if (subAgg instanceof TermsAggregationBuilder && !(field instanceof MethodField)) {

//                        //((TermsAggregationBuilder) subAgg).size(0);
//                    }

                    if (field.isNested()) {
                        Aggregation nestedBuilder = createNestedAggregation(field);

                        if (insertFilterIfExistsAfter(subAgg, groupBy, nestedBuilder, i + 1)) {
                            groupBy.remove(i + 1);
                            i++;
                        } else {
                            nestedBuilder.subAggregation(subAgg);
                        }

                        lastAgg.subAggregation(wrapNestedIfNeeded(nestedBuilder, field.isReverseNested()));
                    } else if (field.isChildren()) {
                        Aggregation childrenBuilder = createChildrenAggregation(field);

                        if (insertFilterIfExistsAfter(subAgg, groupBy, childrenBuilder, i + 1)) {
                            groupBy.remove(i + 1);
                            i++;
                        } else {
                            childrenBuilder.subAggregation(subAgg);
                        }

                        lastAgg.subAggregation(childrenBuilder);
                    } else {
                        lastAgg.subAggregation(subAgg);
                    }

                    lastAgg = subAgg;
                }
            }

            // add aggregation function to each groupBy
            explanFields(request, select.getFields(), lastAgg);
        }

        if (select.getGroupBys().size() < 1) {
            //add aggregation when having no groupBy script
            explanFields(request, select.getFields(), lastAgg);

        }

        Map<String, KVValue> groupMap = aggMaker.getGroupMap();
        // add field
        if (select.getFields().size() > 0) {
            setFields(select.getFields());
//            explanFields(request, select.getFields(), lastAgg);
        }

        // add order
        if (lastAgg != null && select.getOrderBys().size() > 0) {
            for (Order order : select.getOrderBys()) {
                KVValue temp = groupMap.get(order.getName());
                if (temp != null) {
                    TermsAgg termsBuilder = (TermsAgg) temp.value;
                    switch (temp.key) {
                        case "COUNT":
                            termsBuilder.order(BucketOrder.count(isASC(order)));
                            break;
                        case "KEY":
                            termsBuilder.order(BucketOrder.key(isASC(order)));
                            // add the sort to the request also so the results get sorted as well
                            request.addSort(order.getName(), SortOrder.valueOf(order.getType()));
                            break;
                        case "FIELD":
                            termsBuilder.order(BucketOrder.aggregation(order.getName(), isASC(order)));
                            break;
                        default:
                            throw new SqlParseException(order.getName() + " can not to order");
                    }
                } else {
                    request.addSort(order.getName(), SortOrder.valueOf(order.getType()));
                }
            }
        }

        setLimitFromHint(this.select.getHints());

//        request.setSearchType(SearchType.DEFAULT);
        updateRequestWithIndexAndRoutingOptions(select, request);
        updateRequestWithHighlight(select, request);
        updateRequestWithCollapse(select, request);
        updateRequestWithPostFilter(select, request);
        SqlElasticSearchRequestBuilder sqlElasticRequestBuilder = new SqlElasticSearchRequestBuilder(request);
        return sqlElasticRequestBuilder;
    }
    
    private Aggregation getGroupAgg(Field field, Select select2) throws SqlParseException {
        boolean refrence = false;
        Aggregation lastAgg = null;
        for (Field temp : select.getFields()) {
            if (temp instanceof MethodField && temp.getName().equals("script")) {
                MethodField scriptField = (MethodField) temp;
                for (KVValue kv : scriptField.getParams()) {
                    if (kv.value.equals(field.getName())) {
//                        lastAgg = aggMaker.makeGroupAgg(scriptField);
                        refrence = true;
                        break;
                    }
                }
            }
        }

        if (!refrence)
//            lastAgg = aggMaker.makeGroupAgg(field);
        
        return lastAgg;
    }

    private Aggregation wrapNestedIfNeeded(Aggregation nestedBuilder, boolean reverseNested) {
        if (!reverseNested) return nestedBuilder;
//        if (reverseNested && !(nestedBuilder instanceof NestedAggregationBuilder)) return nestedBuilder;
        //we need to jump back to root
//        return Aggregation.reverseNested(nestedBuilder.getName() + "_REVERSED").subAggregation(nestedBuilder);
        return null;
    }

    private Aggregation createNestedAggregation(Field field) {
        Aggregation nestedBuilder;

        String nestedPath = field.getNestedPath();

        if (field.isReverseNested()) {
            if (nestedPath == null || !nestedPath.startsWith("~")) {
//                Aggregation reverseNestedAggregationBuilder = AggregationFactory.reverseNested(getNestedAggName(field));
                Aggregation reverseNestedAggregationBuilder = AggregationFactory.terms(getNestedAggName(field));
                if(nestedPath!=null){
//                    reverseNestedAggregationBuilder.path(nestedPath);
                }
                return reverseNestedAggregationBuilder;
            }
            nestedPath = nestedPath.substring(1);
        }

        nestedBuilder = Aggregation.nested(getNestedAggName(field),nestedPath);

        return nestedBuilder;
    }

    private Aggregation createChildrenAggregation(Field field) {
        Aggregation childrenBuilder=null;

        String childType = field.getChildType();

//        childrenBuilder = JoinAggregationBuilders.children(getChildrenAggName(field),childType);


        return childrenBuilder;
    }

    private String getNestedAggName(Field field) {
        String prefix;

        if (field instanceof MethodField) {
            String nestedPath = field.getNestedPath();
            if (nestedPath != null) {
                prefix = nestedPath;
            } else {
                prefix = field.getAlias();
            }
        } else {
            prefix = field.getName();
        }
        return prefix + "@NESTED";
    }

    private String getChildrenAggName(Field field) {
        String prefix;

        if (field instanceof MethodField) {
            String childType = field.getChildType();

            if (childType != null) {
                prefix = childType;
            } else {
                prefix = field.getAlias();
            }
        } else {
            prefix = field.getName();
        }

        return prefix + "@CHILDREN";
    }

    private boolean insertFilterIfExistsAfter(Aggregation agg, List<Field> groupBy, Aggregation builder, int nextPosition) throws SqlParseException {
        if (groupBy.size() <= nextPosition) return false;
        Field filterFieldCandidate = groupBy.get(nextPosition);
        if (!(filterFieldCandidate instanceof MethodField)) return false;
        MethodField methodField = (MethodField) filterFieldCandidate;
        if (!methodField.getName().toLowerCase().equals("filter")) return false;
        builder.subAggregation(aggMaker.makeGroupAgg(filterFieldCandidate).subAggregation(agg));
        return true;
    }

    private Aggregation updateAggIfNested(Aggregation lastAgg, Field field) {
        if (field.isNested()) {
            lastAgg = AggregationFactory.nested(field.getName() + "Nested",field.getNestedPath())
                    .subAggregation(lastAgg);
        }
        return lastAgg;
    }

    private boolean isASC(Order order) {
        return "ASC".equals(order.getType());
    }

    private void setFields(List<Field> fields) {
        if (select.getFields().size() > 0) {
            ArrayList<String> includeFields = new ArrayList<>();

            for (Field field : fields) {
                if (field != null) {
                    includeFields.add(field.getName());
                }
            }

            request.setFetchSource(includeFields.toArray(new String[includeFields.size()]), null);
        }
    }

    private void explanFields(RestQueryBuilder request, List<Field> fields, Aggregation groupByAgg) throws SqlParseException {
        for (Field field : fields) {
            if (field instanceof MethodField) {

                if (field.getName().equals("script")) {
                    request.addStoredField(field.getAlias());
                    DefaultQueryAction defaultQueryAction = new DefaultQueryAction(client, select);
                    defaultQueryAction.intialize(request);
                    List<Field> tempFields = Lists.newArrayList(field);
                    defaultQueryAction.setFields(tempFields);
                    continue;
                }

                Aggregation makeAgg = aggMaker.makeFieldAgg((MethodField) field, groupByAgg);
                if (groupByAgg != null) {
                    groupByAgg.subAggregation(makeAgg);
                } else {
                    request.addAggregations(makeAgg);
                }
            } else if (field instanceof Field) {
                request.addStoredField(field.getName());
            } else {
                throw new SqlParseException("it did not support this field method " + field);
            }
        }
    }

    /**
     * Create filters based on
     * the Where clause.
     *
     * @param where the 'WHERE' part of the SQL query.
     * @throws SqlParseException
     */
    private void setWhere(Where where) throws SqlParseException {
        if (where != null) {
            QueryBuilder whereQuery = QueryMaker.explan(where,this.select.isQuery);
            request.setQuery(whereQuery);
        }
    }


    /**
     * Set indices and types to the search request.
     */
    private void setIndicesAndTypes() {
        request.setIndices(query.getIndexArr());

        String[] typeArr = query.getTypeArr();
        if (typeArr != null) {
            request.setTypes(typeArr);
        }
    }

    private void setLimitFromHint(List<Hint> hints) {
        int from = 0;
        int size = 0;
        for (Hint hint : hints) {
            if (hint.getType() == HintType.DOCS_WITH_AGGREGATION) {
                Integer[] params = (Integer[]) hint.getParams();
                if (params.length > 1) {
                    // if 2 or more are given, use the first as the from and the second as the size
                    // so it is the same as LIMIT from,size
                    // except written as /*! DOCS_WITH_AGGREGATION(from,size) */
                    from = params[0];
                    size = params[1];
                } else if (params.length == 1) {
                    // if only 1 parameter is given, use it as the size with a from of 0
                    size = params[0];
                }
                break;
            }
        }
        request.setFrom(from);
        request.setSize(size);
    }
}
