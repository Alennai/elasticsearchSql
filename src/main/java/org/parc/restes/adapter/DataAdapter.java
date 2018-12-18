/**
 * @author shaco.zhu
 * Date:2017年5月25日上午9:21:03
 */
package org.parc.restes.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.parc.restes.entity.*;
import org.parc.restes.util.FieldComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;


public class DataAdapter {
    private static DecimalFormat df = new DecimalFormat("#.00");
    private static final Comparator<IField> comparator = new FieldComparator();
    private static final List<String> INCLUDE = Arrays.asList("responseCode", "destGeoRegion",
            "destHostName", "severity", "warningType", "destAddress", "requestUrl", "deviceName ", "ruleId",
            "srcAddress", "srcGeoRegion", "warning", "predictCount", "visitCount");
    private static final Logger logger = LoggerFactory.getLogger(DataAdapter.class);

    public static List<IField> json2Fields(String json, String _type) {
        List<IField> fields = new ArrayList<>();
        Set<String> keySet = new HashSet<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            JSONObject mp = (JSONObject) entry.getValue();
            for (Map.Entry<String, Object> mpEntry : mp.entrySet()) {
                JSONObject type = (JSONObject) mpEntry.getValue();
                for (Map.Entry<String, Object> typeEntry : type.entrySet()) {
                    JSONObject tpp = (JSONObject) typeEntry.getValue();
                    JSONObject pp = (JSONObject) tpp.get("properties");
                    if (pp == null)
                        break;
                    for (Map.Entry<String, Object> ppEntry : pp.entrySet()) {
                        String key = ppEntry.getKey();
                        if (!keySet.contains(key)) {
                            keySet.add(key);
                            fields.add(new IField(key,
                                    (JSONObject) ppEntry.getValue()));
                        }
                    }
                }
            }
        }
        return fields;
    }

    public static Result json2Result(String json) {
        List<JSONObject> list = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        JSONArray documents = (JSONArray) hits.get("hits");
        int total = hits.getIntValue("total");
        int size = documents.size();
        for (int i = 0; i < size; i++) {
            JSONObject source = (JSONObject) documents.get(i);
            JSONObject rs = (JSONObject) source.get("_source");
            rs.put("uuid", source.get("_id"));
            list.add(rs);
        }
        String scroll_id = null;
        if (jsonObject.containsKey("_scroll_id")) {
            scroll_id = jsonObject.getString("_scroll_id");
        }
        JSONObject object = new JSONObject();
        if (jsonObject.containsKey("aggregations")) {
            object = jsonObject.getJSONObject("aggregations");
        }
        return new Result().documents(list).total(total).scrollid(scroll_id).aggregation(object);
    }

    public static JSONArray jsonUpdate(String json, TraceParam traceParam) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        JSONArray documents = (JSONArray) hits.get("hits");
        //add total
        int total = hits.getIntValue("total");
        traceParam.setSize(total > 999 ? 1000 : total);
        int size = documents.size();
        for (int i = 0; i < size; i++) {
            JSONObject source = (JSONObject) documents.get(i);
            updateDocument(source, traceParam);
        }
        return documents;
    }

    public static void updateDocument(JSONObject source, TraceParam traceParam) {
        //update
        JSONObject rs = (JSONObject) source.get("_source");
        String falsePositives = traceParam.getFalsePositives();
        String processingMethod = traceParam.getProcessingMethod();
        rs.put("deal", 1);
//        User user = OtherUtil.getByUUIDser();
//        if (user != null)
//            rs.put("handlingPeople", user.getName());
//        else rs.put("handlingPeople", "system");
        if (falsePositives != null) {
            int falsePtv = Integer.parseInt(falsePositives);
            rs.put("falsePositives", falsePtv);
            if (falsePtv == 0) {
                rs.put("processingMethod", "确认");
            } else {
                rs.put("processingMethod", "忽略");
            }
        }
        if (processingMethod != null) {
            rs.put("processingMethod", traceParam.getProcessingMethod());
            rs.put("handlingComments", traceParam.getHandlingComments());
        }
    }

    public static void updatealarmDoc(JSONObject source, String status) {
        //update
        JSONObject rs = (JSONObject) source.get("_source");
        rs.put("alarmStatus", status);
    }

    public static JSONObject json2OnlyCipDocument(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        JSONArray documents = (JSONArray) hits.get("hits");
        int size = documents.size();
        for (int i = 0; i < size; i++) {
            JSONObject source = documents.getJSONObject(i);
            JSONObject rs = source.getJSONObject("_source");
            if ((rs.containsKey("lowAddress") && rs.containsKey("highAddress")) || rs.containsKey("onlyIp")) {
                return rs;
            }
            rs.put("uuid", source.get("_id"));
        }
        return null;
    }

    public static JSONObject json2SingleDocument(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        JSONArray documents = hits.getJSONArray("hits");
        if (!documents.isEmpty()) {
            return documents.getJSONObject(0).getJSONObject("_source");
        }
        return null;
    }

    public static Result json2Stack(String json) {
        Stack<JSONObject> stack = new Stack<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = jsonObject.getJSONObject("hits");
        JSONArray documents = hits.getJSONArray("hits");
        int total = hits.getIntValue("total");
        int size = documents.size();
        for (int i = 0; i < size; i++) {
            JSONObject source = (JSONObject) documents.get(i);
            JSONObject rs = (JSONObject) source.get("_source");
            rs.put("uuid", source.get("_id"));
            stack.add(rs);
        }
        return new Result().stack(stack).total(total);
    }

    public static Result json2StackPop(String json, int fSize) {
        Stack<JSONObject> stack = new Stack<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = jsonObject.getJSONObject("hits");
        JSONArray documents = hits.getJSONArray("hits");
        int total = hits.getIntValue("total");
        int size = documents.size();
        Set<String> filter = new HashSet<>();
        for (int i = 0; i < size; i++) {
            JSONObject source = (JSONObject) documents.get(i);
            JSONObject rs = (JSONObject) source.get("_source");
            rs.put("uuid", source.get("_id"));
            rs.get("destHostName");
            rs.get("warningType");
            stack.add(rs);
        }
        return new Result().stack(stack).total(total);
    }

    public static List<Map<String, String>> adapterProof(Stack<JSONObject> documents) {
        Iterator<JSONObject> it = documents.iterator();
        List<Map<String, String>> list = new ArrayList<>();
        while (it.hasNext()) {
            try {
                list.add(createProofMsg(it.next()));
            } catch (Exception e) {
                logger.error("ex:{}", e);
            }
        }
        return list;
    }

    private static Map<String, String> createProofMsg(JSONObject obj) {
        String alertType = obj.getString("web_alert");
        String type = obj.getString("name");
//        String typeName = DictionaryCache.fieldByCategory("warningType", type);
        String srcAddress = obj.getString("srcAddress");
        String destHostName = obj.getString("destHostName");
        String destAddress = obj.getString("destAddress");
        String ruleName = obj.getString("ruleName");
        String ruleId = obj.getString("ruleId");
        int stageCode = obj.containsKey("stageCode") ? obj.getInteger("stageCode") : 1;
//        if (StringUtils.isBlank(destHostName))
//            destHostName = StringUtils.isBlank(destAddress) ? "-" : destAddress;
        String name = obj.getString("name");
        String time = obj.getString("@timestamp");
//        time = DateUtil.utc2localStr(time);
        String msg = "";
        if ("10000011".equals(ruleId) || "10000012".equals(ruleId)) {
            msg = String.format("%s 对 %s 进行(%s)%s", srcAddress, destHostName, name, ruleName);
        } else if ("securityevent".equals(alertType)) {
//            msg = String.format(ESConstant.MSG, srcAddress, destHostName, name);
        } else {
//            msg = String.format(ESConstant.MSG, srcAddress, destHostName, name);
        }
        return ImmutableMap.of("time", time, "msg", msg, "sign",
//                DictionaryCache.fieldByCategory("stageCode", stageCode + "")
 "");
    }

    public static Map<String, Object> adapterGangsAnalysis(List<JSONObject> documents) {
        List<Map<String, Object>> outer = new ArrayList<>();
        for (JSONObject doc : documents) {
            String ip = doc.getString("ip");
            String _cre = doc.getString("credibility");
            if (StringUtils.isBlank(_cre))
                continue;
            JSONObject cre = JSON.parseObject(_cre);
            JSONObject scene = cre.getJSONObject("dimension_scene");
            Iterator<String> itK = scene.keySet().iterator();
            List<Map<String, Object>> tmp = new ArrayList<>();
            int count = 0;
            while (itK.hasNext()) {
                if (count > 3)
                    break;
//                tmp.add(adapterNV(DictionaryCache.fieldByCategory("securityevent", itK.next())));
                count++;
            }
            if (tmp.isEmpty()) {
                outer.add(adapterNV(ip));
            } else {
                outer.add(adapterKC(ip, tmp));
            }
        }
        return outer.isEmpty() ? adapterNV("团伙分析") : adapterKC("团伙分析", outer);
    }



    private static Map<String, Object> adapterNV(String value) {
        Map<String, Object> TMP = new HashMap<>();
        TMP.put("name", value);
        return TMP;
    }


    public static Map<String, Object> adapterST(String source, String target) {
        Map<String, Object> TMP = new HashMap<>();
        TMP.put("source", source);
        TMP.put("target", target);
        return TMP;
    }

    public static Map<String, Object> adapterKV(String key, String value) {
        Map<String, Object> TMP = new HashMap<>();
        TMP.put("name", key);
        TMP.put("children", new Object[]{ImmutableMap.of("name", value)});
        return TMP;
    }

    private static Map<String, Object> adapterKC(String key, List<Map<String, Object>> children) {
        Map<String, Object> TMP = new HashMap<>();
        TMP.put("name", key);
        TMP.put("children", children);
        return TMP;
    }


    public static List<Bucket> json2SingleField(String json, String name) {
        List<Bucket> buckets = new ArrayList<>();
        if (StringUtils.isBlank(json))
            return buckets;
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        int total = hits.getIntValue("total");
        if (total == 0)
            return buckets;
        JSONObject aggregations = (JSONObject) jsonObject.get("aggregations");
        JSONObject agg = (JSONObject) aggregations.get(name);
        JSONArray bks = (JSONArray) agg.get("buckets");
        int size = bks.size();
        for (int i = 0; i < size; i++) {
            JSONObject source = (JSONObject) bks.get(i);
            buckets.add(new Bucket(name, source.getString("key"), source.getIntValue("doc_count"), total));
        }
        return buckets;
    }


    private static void recursiveDig(JSONObject root, String source, Stack<String> stack,
                                     List<Map<String, String>> first, List<String> second) {
        Set<String> keys = root.keySet();
        String key = getNode(keys);
        if (StringUtils.isNotBlank(key)) {
            JSONObject node = root.getJSONObject(key);
            Set<String> nodeSet = node.keySet();
            if (nodeSet.contains("buckets")) {
                JSONArray array = node.getJSONArray("buckets");
                Iterator<Object> tmpIt = array.iterator();
                while (tmpIt.hasNext()) {
                    JSONObject jb = (JSONObject) tmpIt.next();
                    stack.add(key);
                    String _key = jb.getString("key");
                    String _key_2 = stack.peek() + " " + _key;
                    if (!second.contains(_key_2))
                        second.add(_key_2);
                    if (StringUtils.isNotBlank(source)) {
                        String lasted = stack.peek();
                        if (StringUtils.isBlank(lasted)) {
                            stack.pop();
                            lasted = stack.peek();
                        } else {
                            lasted = stack.pop();
                        }
                        first.add(createSTCMap(source, lasted + " " + _key, jb.getString("doc_count")));
                    }
                    recursiveDig(jb, key + " " + jb.getString("key"), stack, first, second);

                }
            }
        }
    }

    private static String getNode(Set<String> keys) {
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            if ("doc_count".equals(key) || "key".equals(key) || "sum_other_doc_count".equals(key)
                    || "doc_count_error_upper_bound".equals(key)) {
                continue;
            }
            return key;
        }
        return "";
    }

    private static Map<String, Object> createFSMap(Object first, Object second) {
        Map<String, Object> tmp = new HashMap<>();
        tmp.put("first", first);
        tmp.put("second", second);
        return tmp;
    }

    private static Map<String, String> createSTCMap(String source, String target, String count) {
        Map<String, String> tmp = new HashMap<>();
//        tmp.put("source", DictionaryCache.fieldByCategoryBySpace("securityevent", source, true));
//        tmp.put("target", DictionaryCache.fieldByCategoryBySpace("securityevent", target, true));
        tmp.put("source_key", source);
        tmp.put("target_key", target);
        tmp.put("count", count);
        tmp.put("value", count);
        return tmp;
    }

    public static int jsonDrillCount(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.getIntValue("count");
    }

    public static List<Bucket> jsonDrillBucket(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        int total = hits.getIntValue("total");
        List<Bucket> targets = new ArrayList<>();
        if (total == 0)
            return targets;
        JSONObject aggregations = jsonObject.getJSONObject("aggregations");
        recursiveDig(aggregations, targets, null);
        return targets;
    }

    private static void recursiveDig(JSONObject root, List<Bucket> buckets, Bucket current) {
        Set<String> keys = root.keySet();
        String key = getNode(keys);
        if (StringUtils.isNotBlank(key)) {
            JSONObject tmp = root.getJSONObject(key);
            JSONArray bkts = tmp.getJSONArray("buckets");
            int size = bkts.size();
            for (int i = 0; i < size; i++) {
                JSONObject tgt = bkts.getJSONObject(i);
                JSONArray arrayTmp = tgt.getJSONArray("buckets");
                String k = tgt.getString("key");
                Integer c = tgt.getInteger("doc_count");
                Bucket bkt = new Bucket(key, k, c);
                if (arrayTmp == null && current != null) {
                    current.addBucket(bkt);
                    recursiveDig(tgt, buckets, bkt);
                } else {
                    buckets.add(bkt);
                    recursiveDig(tgt, buckets, bkt);
                }
            }
        }
    }


    public static Map<String, Object> seriesByDateHistogram(String json, String timeStr) {
        Map<String, Object> result = new HashMap<>();
        Stack<Integer> seriesData = new Stack<>();
        Stack<String> xAxis = new Stack<>();
        result.put("series", ImmutableList.of(ImmutableMap.of("data", seriesData)));
        result.put("xAxis", xAxis);
        if (StringUtils.isBlank(json)) {
            return result;
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = jsonObject.getJSONObject("hits");
        int total = hits.getIntValue("total");
        JSONObject aggregations = jsonObject.getJSONObject("aggregations");
        JSONObject agg = aggregations.getJSONObject("@timestamp");
        JSONArray bks = agg.getJSONArray("buckets");
        int size = bks.size();
        for (int i = 0; i < size; i++) {
            JSONObject source = bks.getJSONObject(i);
            Date cDate = new Date(source.getLong("key"));
            xAxis.add(ElasticDate.format(cDate, timeStr));
            seriesData.add(source.getIntValue("doc_count"));
        }
        return result;
    }

    public static Map<String, String> json2Map(String string) {
        Map<String, String> ipFsUsage = new HashMap<>();
        JSONObject json = JSONObject.parseObject(string);
        //单个几点会是node吗？
        if (json.containsKey("nodes")) {
            JSONObject node = json.getJSONObject("nodes");
            for (String nodeid : node.keySet()) {
                JSONObject nodeItem = node.getJSONObject(nodeid);
                double free = nodeItem.getJSONObject("fs").getJSONObject("total").getDoubleValue("free_in_bytes");
                double total = nodeItem.getJSONObject("fs").getJSONObject("total").getDoubleValue("total_in_bytes");
                ipFsUsage.put(nodeItem.getString("name"), df.format(100 - free / total * 100) + "," + total);
            }
        }
        return ipFsUsage;
    }
}
