/**
 * @author shaco.zhu
 * Date:2017年5月25日上午9:21:03
 */
package org.parc.restes.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dbapp.cpsysportal.cache.DictionaryCache;
import com.dbapp.cpsysportal.elasticsearch.entity.*;
import com.dbapp.cpsysportal.elasticsearch.util.ESConstant;
import com.dbapp.cpsysportal.elasticsearch.util.FieldComparator;
import com.dbapp.cpsysportal.entity.User;
import com.dbapp.elasticsearch.ElasticDate;
import com.dbapp.utils.DateUtil;
import com.dbapp.utils.OtherUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.dbapp.cpsysportal.elasticsearch.util.ESConstant.df;

public class DataAdapter {
    private static final Comparator<IField> comparator = new FieldComparator();
    private static final List<String> INCLUDE = Arrays.asList(new String[]{"responseCode", "destGeoRegion",
            "destHostName", "severity", "warningType", "destAddress", "requestUrl", "deviceName ", "ruleId",
            "srcAddress", "srcGeoRegion", "warning", "predictCount", "visitCount"});
    private static final Logger logger = LoggerFactory.getLogger(DataAdapter.class);

    public static List<com.dbapp.cpsysportal.elasticsearch.entity.IField> json2Fields(String json, String _type) {
        List<com.dbapp.cpsysportal.elasticsearch.entity.IField> fields = new ArrayList<>();
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
                        // if (!"search".equals(_type) && !INCLUDE.contains(key)
                        // || "shacoTime".equals(key)) {
                        // continue;
                        // }
                        if (!keySet.contains(key)) {
                            keySet.add(key);
                            fields.add(new com.dbapp.cpsysportal.elasticsearch.entity.IField(key,
                                    (JSONObject) ppEntry.getValue()));
                        }
                    }
                }
            }
        }
        // Collections.sort(fields, comparator);
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
        User user = OtherUtil.getUser();
        if (user != null)
            rs.put("handlingPeople", user.getName());
        else rs.put("handlingPeople", "system");
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
        String typeName = DictionaryCache.fieldByCategory("warningType", type);
        String srcAddress = obj.getString("srcAddress");
        String destHostName = obj.getString("destHostName");
        String destAddress = obj.getString("destAddress");
        String ruleName = obj.getString("ruleName");
        String ruleId = obj.getString("ruleId");
        int stageCode = obj.containsKey("stageCode") ? obj.getInteger("stageCode") : 1;
        if (StringUtils.isBlank(destHostName))
            destHostName = StringUtils.isBlank(destAddress) ? "-" : destAddress;
        String name = obj.getString("name");
        String time = obj.getString("@timestamp");
        time = DateUtil.utc2localStr(time);
        String msg = "";
        if ("10000011".equals(ruleId) || "10000012".equals(ruleId)) {
            msg = String.format("%s 对 %s 进行(%s)%s", srcAddress, destHostName, name, ruleName);
        } else if ("securityevent".equals(alertType)) {
            msg = String.format(ESConstant.MSG, srcAddress, destHostName, name);
        } else {
            msg = String.format(ESConstant.MSG, srcAddress, destHostName, name);
        }
        return ImmutableMap.of("time", time, "msg", msg, "sign", DictionaryCache.fieldByCategory("stageCode", stageCode + ""));
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
                tmp.add(adapterNV(DictionaryCache.fieldByCategory("securityevent", itK.next())));
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

    public static Map<String, Object> adapterIpRepository(List<JSONObject> documents) {
        Set<String> bak = new HashSet<>();
        List<Map<String, Object>> children = new ArrayList<>();
        Stack<Map<String, Object>> children_2 = new Stack<>();
        Stack<Map<String, Object>> children_3 = new Stack<>();
        for (JSONObject doc : documents) {
            String type = doc.getString("type");
            if (!bak.contains(type)) {
                bak.add(type);
                if (ESConstant.type_idc.equals(type)) {
                    children.add(adapterIRepository(doc, type));
                } else if (ESConstant.type_scan201610.equals(type)) {
                    children.add(adapterIRepository(doc, type));
                } else if (ESConstant.type_record_information.equals(type)) {
                    children_3.add(adapterTopSpecField(doc, "companyName", type));
                }
            }
            if (ESConstant.type_scan20170312.equals(type)) {
                String port = doc.getString("port");
                bak.add(port);
                if (bak.contains(port)) {
                    continue;
                }
                children_2.add(adapterTopSpecField(doc, "port", type));
            }
        }
        int c_1 = children.size();
        int c_2 = children_2.size();
        int c_3 = children_3.size();
        int capacity = ESConstant.IP_LIMIT - c_1;
        int sub_2 = c_2 - capacity;
        if (sub_2 > 0) {
            while (sub_2 > 0) {
                children_2.pop();
                sub_2--;
            }
        } else {
            capacity = capacity - c_2;
            int sub_3 = c_3 - capacity;
            while (sub_3 > 0) {
                children_3.pop();
                sub_3--;
            }
        }
        if (!children_2.isEmpty()) {
            children.add(adapterKC(DictionaryCache.fieldByCategory("iprepository", ESConstant.type_scan20170312), children_2));
        }
        if (!children_3.isEmpty()) {
            children.add(
                    adapterKC(DictionaryCache.fieldByCategory("iprepository", ESConstant.type_record_information), children_3));
        }
        if (children.isEmpty()) {
            return adapterNV("基础信息");
        }
        return adapterKC("基础信息", children);
    }

    public static Map<String, Object> adapterIpRepositoryRecordInfomation(List<JSONObject> documents) {
        List<Map<String, Object>> children = new ArrayList<>();
        for (JSONObject doc : documents) {
            String type = doc.getString("type");
            if (ESConstant.type_record_information.equals(type)) {
                children.add(adapterTopSpecField(doc, "companyName", type));
            }
        }
        if (children.isEmpty()) {
            return adapterNV("攻击对象信息");
        }
        return adapterKC("攻击对象信息", children);
    }

    public static Map<String, Object> adapterTopSpecField(JSONObject obj, String field, String type) {
        String port = obj.getString(field);
        if (StringUtils.isBlank(port)) {
            return null;
        }
        List<Map<String, Object>> children = new ArrayList<>();
        Iterator<String> it = obj.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key.equals(field)) {
                continue;
            }
            String kDic = DictionaryCache.fieldByCategory(type + "_field", key);
            if (!key.equals(kDic)) {
                String value = obj.getString(key);
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                children.add(adapterKV(kDic, value));
            }
        }
        return adapterKC(port, children);
    }

    public static Map<String, Object> adapterIRepository(JSONObject obj, String type) {
        Iterator<String> it = obj.keySet().iterator();
        List<Map<String, Object>> children = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            String kDic = DictionaryCache.fieldByCategory(type + "_field", key);
            if (!key.equals(kDic)) {
                String value = obj.getString(key);
                if (StringUtils.isBlank(value)) {
                    continue;
                }
                children.add(adapterKV(kDic, value));
            }
        }
        return adapterKC(DictionaryCache.fieldByCategory("iprepository", type), children);
    }

    public static Map<String, Object> adapterNV(String value) {
        Map<String, Object> TMP = new HashMap<>();
        TMP.put("name", value);
        return TMP;
    }

    public static Map<String, Object> adapterNVCatrgory(String value) {
        Map<String, Object> TMP = new HashMap<>();
        TMP.put("name", value);
        TMP.put("category", DictionaryCache.isReallyAttackAlert(value));
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

    public static Map<String, Object> adapterKC(String key, List<Map<String, Object>> children) {
        Map<String, Object> TMP = new HashMap<>();
        TMP.put("name", key);
        TMP.put("children", children);
        return TMP;
    }

    public static BdResponse json2BdResponse(String json) {
        BdResponse resp = new BdResponse();
        resp.setCode(0 + "");
        resp.setRespMsg(json);
        return resp;
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

    public static Map<String, Object> jsonDrillAgg(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject hits = (JSONObject) jsonObject.get("hits");
        int total = hits.getIntValue("total");
        if (total == 0)
            return new HashMap<>();
        JSONObject aggregations = jsonObject.getJSONObject("aggregations");
        List<Map<String, String>> first = new ArrayList<>();
        List<String> second = new ArrayList<>();
        Stack<String> stack = new Stack<String>();
        recursiveDig(aggregations, "", stack, first, second);
        List<Map<String, String>> convertSecond = new ArrayList<>();
        for (String tmp : second) {
            convertSecond.add(ImmutableMap.of("key", tmp, "name",
                    DictionaryCache.fieldByCategoryBySpace("securityevent", tmp, true)));
        }
        logger.debug(JSON.toJSONString(createFSMap(first, convertSecond)));
        return createFSMap(first, convertSecond);
    }

    public static void recursiveDig(JSONObject root, String source, Stack<String> stack,
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

    public static String getNode(Set<String> keys) {
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
        tmp.put("source", DictionaryCache.fieldByCategoryBySpace("securityevent", source, true));
        tmp.put("target", DictionaryCache.fieldByCategoryBySpace("securityevent", target, true));
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

    public static void recursiveDig(JSONObject root, List<Bucket> buckets, Bucket current) {
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

    public static Map<String, Object> bucketsToAttackAnalysis(List<Bucket> buckets) {
        List<Map<String, Object>> attackObject = new ArrayList<>();
        for (Bucket b : buckets) {
            String k = b.getKey();
            List<Map<String, Object>> step_0 = new ArrayList<>();// attack
            // object
            List<Map<String, Object>> step_1 = new ArrayList<>();
            List<Map<String, Object>> step_2 = new ArrayList<>();
            List<Bucket> sub_1 = b.getSubs();
            if (sub_1 != null) {
                List<Map<String, Object>> step_1_2 = new ArrayList<>();
                for (Bucket tmp_1 : sub_1) {
                    // process warningType
                    String k1 = tmp_1.getKey();
                    List<Bucket> sub_2 = tmp_1.getSubs();
                    if (sub_2 != null) {
                        List<Map<String, Object>> step_1_3 = new ArrayList<>();
                        for (Bucket tmp_2 : sub_2) {
                            step_1_3.add(adapterNV(tmp_2.getKey()));
                        }
                        step_1_2.add(adapterKC(ESConstant.ATTACK_PROOF, step_1_3));
                    }
                    step_1.add(adapterKC(k1, step_1_2));
                }
            }
            // -------------------------------------
            step_0.add(adapterKC(ESConstant.ATTACK_TYPE, step_1));
            step_0.add(adapterKC(ESConstant.ATTACK_TARGET_INFO, step_2));
            attackObject.add(adapterKC(k, step_0));
        }
        return adapterKC(ESConstant.ATTACK_ANALYSIS, attackObject);
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
        Map<String, String> ipFsUsage = new HashMap<String, String>();
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
