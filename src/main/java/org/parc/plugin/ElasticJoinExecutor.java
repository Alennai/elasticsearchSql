package org.parc.plugin;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.parc.sqlrestes.domain.Field;
import org.parc.sqlrestes.exception.SqlParseException;
import org.parc.sqlrestes.query.SqlElasticRequestBuilder;
import org.parc.sqlrestes.query.join.HashJoinElasticRequestBuilder;
import org.parc.sqlrestes.query.join.JoinRequestBuilder;
import org.parc.sqlrestes.query.join.NestedLoopsElasticRequestBuilder;
import org.parc.sqlrestes.query.join.TableInJoinRequestBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xusiao on 2018/6/20.
 */
public abstract class ElasticJoinExecutor implements ElasticHitsExecutor {
    final int MAX_RESULTS_ON_ONE_FETCH = 10000;
    private SearchHits results;
    private MetaSearchResult metaResults;
    private Set<String> aliasesOnReturn;
    private boolean allFieldsReturn;

    ElasticJoinExecutor(JoinRequestBuilder requestBuilder) {
        metaResults = new MetaSearchResult();
        aliasesOnReturn = new HashSet<>();
        List<Field> firstTableReturnedField = requestBuilder.getFirstTable().getReturnedFields();
        List<Field> secondTableReturnedField = requestBuilder.getSecondTable().getReturnedFields();
        allFieldsReturn = (firstTableReturnedField == null || firstTableReturnedField.size() == 0)
                && (secondTableReturnedField == null || secondTableReturnedField.size() == 0);
    }

    public static ElasticJoinExecutor createJoinExecutor(RestClient client, SqlElasticRequestBuilder requestBuilder) {
        if (requestBuilder instanceof HashJoinElasticRequestBuilder) {
            HashJoinElasticRequestBuilder hashJoin = (HashJoinElasticRequestBuilder) requestBuilder;
            return new HashJoinElasticExecutor(client, hashJoin);
        } else if (requestBuilder instanceof NestedLoopsElasticRequestBuilder) {
            NestedLoopsElasticRequestBuilder nestedLoops = (NestedLoopsElasticRequestBuilder) requestBuilder;
            return new NestedLoopsElasticExecutor(client, nestedLoops);
        } else {
            throw new RuntimeException("Unsuported requestBuilder of type: " + requestBuilder.getClass());
        }
    }

    public void sendResponse(RestChannel channel) {
        try {
            String json = ElasticUtils.hitsAsStringResult(results, metaResults);
            BytesRestResponse bytesRestResponse = new BytesRestResponse(RestStatus.OK, json);
            channel.sendResponse(bytesRestResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() throws IOException, SqlParseException {
        long timeBefore = System.currentTimeMillis();
        List<SearchHit> combinedSearchHits = innerRun();
        int resultsSize = combinedSearchHits.size();
        SearchHit[] hits = combinedSearchHits.toArray(new SearchHit[resultsSize]);
        this.results = new SearchHits(hits, resultsSize, 1.0f);
        long joinTimeInMilli = System.currentTimeMillis() - timeBefore;
        this.metaResults.setTookImMilli(joinTimeInMilli);
    }

    protected abstract List<SearchHit> innerRun() throws SqlParseException;

    @Override
    public SearchHits getHits() {
        return results;
    }

    void mergeSourceAndAddAliases(Map<String, Object> secondTableHitSource, SearchHit searchHit, String t1Alias, String t2Alias) {
        Map<String, Object> results = mapWithAliases(searchHit.getSourceAsMap(), t1Alias);
        results.putAll(mapWithAliases(secondTableHitSource, t2Alias));
        searchHit.getSourceAsMap().clear();
        searchHit.getSourceAsMap().putAll(results);
    }

    private Map<String, Object> mapWithAliases(Map<String, Object> source, String alias) {
        Map<String, Object> mapWithAliases = new HashMap<>();
        for (Map.Entry<String, Object> fieldNameToValue : source.entrySet()) {
            if (!aliasesOnReturn.contains(fieldNameToValue.getKey())) {
                mapWithAliases.put(alias + "." + fieldNameToValue.getKey(), fieldNameToValue.getValue());
            } else {
                mapWithAliases.put(fieldNameToValue.getKey(), fieldNameToValue.getValue());
            }
        }
        return mapWithAliases;
    }

    void onlyReturnedFields(Map<String, Object> fieldsMap, List<Field> required, boolean allRequired) {
        HashMap<String, Object> filteredMap = new HashMap<>();
        if (allFieldsReturn || allRequired) {
            filteredMap.putAll(fieldsMap);
            return;
        }
        for (Field field : required) {
            String name = field.getName();
            String returnName = name;
            String alias = field.getAlias();
            if (alias != null && alias != "") {
                returnName = alias;
                aliasesOnReturn.add(alias);
            }
            filteredMap.put(returnName, deepSearchInMap(fieldsMap, name));
        }
        fieldsMap.clear();
        fieldsMap.putAll(filteredMap);

    }

    Object deepSearchInMap(Map<String, Object> fieldsMap, String name) {
        if (name.contains(".")) {
            String[] path = name.split("\\.");
            Map<String, Object> currentObject = fieldsMap;
            for (int i = 0; i < path.length - 1; i++) {
                Object valueFromCurrentMap = currentObject.get(path[i]);
                if (valueFromCurrentMap == null) {
                    return null;
                }
                if (!Map.class.isAssignableFrom(valueFromCurrentMap.getClass())) {
                    return null;
                }
                currentObject = (Map<String, Object>) valueFromCurrentMap;
            }
            return currentObject.get(path[path.length - 1]);
        }

        return fieldsMap.get(name);
    }


    void addUnmatchedResults(List<SearchHit> combinedResults, Collection<SearchHitsResult> firstTableSearchHits, List<Field> secondTableReturnedFields, int currentNumOfIds, int totalLimit, String t1Alias, String t2Alias) {
        boolean limitReached = false;
        for (SearchHitsResult hitsResult : firstTableSearchHits) {
            if (!hitsResult.isMatchedWithOtherTable()) {
                for (SearchHit hit : hitsResult.getSearchHits()) {

                    //todo: decide which id to put or type. or maby its ok this way. just need to doc.
                    SearchHit unmachedResult = createUnmachedResult(secondTableReturnedFields, hit.docId(), t1Alias, t2Alias, hit);
                    combinedResults.add(unmachedResult);
                    currentNumOfIds++;
                    if (currentNumOfIds >= totalLimit) {
                        limitReached = true;
                        break;
                    }

                }
            }
            if (limitReached) {
                break;
            }
        }
    }

    SearchHit createUnmachedResult(List<Field> secondTableReturnedFields, int docId, String t1Alias, String t2Alias, SearchHit hit) {
        String unmatchedId = hit.getId() + "|0";
        Text unamatchedType = new Text(hit.getType() + "|null");

        SearchHit searchHit = new SearchHit(docId, unmatchedId, unamatchedType, hit.getFields());

        searchHit.sourceRef(hit.getSourceRef());
        searchHit.getSourceAsMap().clear();
        searchHit.getSourceAsMap().putAll(hit.getSourceAsMap());
        Map<String, Object> emptySecondTableHitSource = createNullsSource(secondTableReturnedFields);

        mergeSourceAndAddAliases(emptySecondTableHitSource, searchHit, t1Alias, t2Alias);

        return searchHit;
    }

    private Map<String, Object> createNullsSource(List<Field> secondTableReturnedFields) {
        Map<String, Object> nulledSource = new HashMap<>();
        for (Field field : secondTableReturnedFields) {
            if (!field.getName().equals("*")) {
                nulledSource.put(field.getName(), null);
            }
        }
        return nulledSource;
    }

    void updateMetaSearchResults(SearchResponse searchResponse) {
        this.metaResults.addSuccessfulShards(searchResponse.getSuccessfulShards());
        this.metaResults.addFailedShards(searchResponse.getFailedShards());
        this.metaResults.addTotalNumOfShards(searchResponse.getTotalShards());
        this.metaResults.updateTimeOut(searchResponse.isTimedOut());
    }

    protected SearchResponse scrollOneTimeWithMax(Client client, TableInJoinRequestBuilder tableRequest) {
        SearchResponse responseWithHits = null;
//        SearchRequestBuilder scrollRequest = tableRequest.getRequestBuilder()
//                .setScroll(new TimeValue(60000))
//                .setSize(MAX_RESULTS_ON_ONE_FETCH);
        boolean ordered = tableRequest.getOriginalSelect().isOrderdSelect();
//        if(!ordered) scrollRequest.addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC);
//        responseWithHits = scrollRequest.get();
        //on ordered select - not using SCAN , elastic returns hits on first scroll
        //es5.0 elastic always return docs on scan
//        if(!ordered)
//            responseWithHits = client.prepareSearchScroll(responseWithHits.getScrollId()).setScroll(new TimeValue(600000)).get();
        return responseWithHits;
    }


}
