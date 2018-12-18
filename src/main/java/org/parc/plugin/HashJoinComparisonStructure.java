package org.parc.plugin;

import org.elasticsearch.search.SearchHit;
import org.parc.sqlrestes.domain.Field;

import java.util.*;

/**
 * Created by xusiao on 2018/6/20.
 */
class HashJoinComparisonStructure {
    private HashMap<String,List<Map.Entry<Field,Field>>> comparisonIDtoComparisonFields;
    private HashMap<String,HashMap<String,SearchHitsResult>> comparisonIDtoComparisonHash;

    public HashJoinComparisonStructure(List<List<Map.Entry<Field, Field>>> t1ToT2FieldsComparisons) {
        comparisonIDtoComparisonFields = new HashMap<>();
        comparisonIDtoComparisonHash = new HashMap<>();
        if(t1ToT2FieldsComparisons == null || t1ToT2FieldsComparisons.size()  == 0){
            String comparisonId = UUID.randomUUID().toString();
            this.comparisonIDtoComparisonFields.put(comparisonId, new ArrayList<>());
            this.comparisonIDtoComparisonHash.put(comparisonId, new HashMap<>());
        }
        for (List<Map.Entry<Field,Field>> comparisonFields : t1ToT2FieldsComparisons){
            String comparisonId = UUID.randomUUID().toString();
            //maby from field to List<IDS> ?
            this.comparisonIDtoComparisonFields.put(comparisonId,comparisonFields);
            this.comparisonIDtoComparisonHash.put(comparisonId, new HashMap<>());
        }
    }

    public HashMap<String, List<Map.Entry<Field, Field>>> getComparisons() {
        return comparisonIDtoComparisonFields;
    }

    public void insertIntoComparisonHash(String comparisonID,String comparisonKey,SearchHit hit){
        HashMap<String, SearchHitsResult> comparisonHash = this.comparisonIDtoComparisonHash.get(comparisonID);
        SearchHitsResult currentSearchHitsResult = comparisonHash.get(comparisonKey);
        if(currentSearchHitsResult == null) {
            currentSearchHitsResult = new SearchHitsResult(new ArrayList<>(),false);
            comparisonHash.put(comparisonKey, currentSearchHitsResult);
        }
        currentSearchHitsResult.getSearchHits().add(hit);
    }

    public SearchHitsResult searchForMatchingSearchHits(String comparisonID,String comparisonKey){
        HashMap<String, SearchHitsResult> comparisonHash = this.comparisonIDtoComparisonHash.get(comparisonID);
        return comparisonHash.get(comparisonKey);
    }

    public List<SearchHitsResult> getAllSearchHits(){
        List<SearchHitsResult> allSearchHits = new ArrayList<>();

        for(HashMap<String, SearchHitsResult> comparisonHash : this.comparisonIDtoComparisonHash.values())
            allSearchHits.addAll(comparisonHash.values());
        return allSearchHits;
    }

}
