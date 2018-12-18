/**
 * @author shaco.zhu
 * @email shaco.zhu@dbappsecurity.com.cn
 * Date:2017年7月25日
 */
package org.parc.restes.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.parc.restes.adapter.DataAdapter;
import org.parc.restes.entity.*;
import org.parc.restes.service.IEsService;
import org.parc.restes.util.CurlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static org.parc.restes.util.ESConstant.ALERT_INDEX_MAPPING_POINT;
import static org.parc.restes.util.ESConstant.ALL_INDEX_MAPPING_POINT;


public class IEsServiceImpl implements IEsService {
    private Map<String, Map<String, Field>> FIELD_GROUP_MAP = new ConcurrentHashMap<>();
    private RestClient client;
    private static Map<String, List<Map<String, Object>>> fields = new ConcurrentHashMap<>();
    private final ICategory OTHER_FIELD = new ICategory("other_field", "其他字段", 9999);
    private static final Logger logger = LoggerFactory.getLogger(IEsServiceImpl.class);
    private String ip,restPort;

    private IEsServiceImpl(String jdbcUrl) {
        String hostAndPortArrayStr = jdbcUrl.split("/")[2];
        String[] hostAndPortArray = hostAndPortArrayStr.split(",");
        HttpHost[] hosts = new HttpHost[hostAndPortArray.length];
        for ( int i=0;i<hostAndPortArray.length;i++) {
            String hostAndPort=hostAndPortArray[i];
            String host = hostAndPort.split(":")[i];
            String port = hostAndPort.split(":")[i];
            hosts[i] = new HttpHost(host, Integer.parseInt(port), "http");
        }
        client = RestClient.builder(hosts).setMaxRetryTimeoutMillis(10000).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.dbapp.cpsysportal.service.ITraceService#categories(java.lang.String)7
     */
    @Override
    public Field categories(String type, String fileName) {
        Map<String, Field> _tmp = FIELD_GROUP_MAP.get(type);
        if (_tmp != null) {
            return _tmp.get(fileName);
        }
        return null;
    }

    @Override
    public synchronized List<Map<String, Object>> get_field_by_type(String type) throws IOException {
        if (type == null)
            type = "search";
        List<Map<String, Object>> _fields = fields.get(type);
        if (_fields != null && !_fields.isEmpty()) {
            return _fields;
        }
        Response resp = client.performRequest("GET", getFieldPointByType(type), Collections.<String, String>emptyMap());
        Map<ICategory, List<IField>> ic_l_if = json2Fields(EntityUtils.toString(resp.getEntity()), type);
        _fields = new ArrayList<>();
        for (Entry<ICategory, List<IField>> entry : ic_l_if.entrySet()) {
            ICategory ick = entry.getKey();
            _fields.add(ImmutableMap.of("name", ick.getName(), "order", ick.getOrder(), "fields", entry.getValue()));
        }
        Collections.sort(_fields, new Comparator<Map<String, Object>>() {

            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Object oo1 = o1.get("order");
                Object oo2 = o2.get("order");
                if (oo1 != null && oo2 != null) {
                    return NumberUtils.toInt(oo1.toString()) - NumberUtils.toInt(oo2.toString());
                }
                return 0;
            }

        });
        return _fields;

    }

    private Map<ICategory, List<IField>> json2Fields(String json, String _type) {
        Map<ICategory, List<IField>> map = new HashMap<>();
        Set<String> keySet = new HashSet<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        for (Entry<String, Object> entry : jsonObject.entrySet()) {
            JSONObject mp = (JSONObject) entry.getValue();
            for (Entry<String, Object> mpEntry : mp.entrySet()) {
                JSONObject type = (JSONObject) mpEntry.getValue();
                for (Entry<String, Object> typeEntry : type.entrySet()) {
                    JSONObject tpp = (JSONObject) typeEntry.getValue();
                    JSONObject pp = (JSONObject) tpp.get("properties");
                    if (pp == null)
                        break;
                    for (Entry<String, Object> ppEntry : pp.entrySet()) {
                        String key = ppEntry.getKey();
                        if (!keySet.contains(key)) {
                            keySet.add(key);
                            Field _tmp_f = categories(_type, key);
                            ICategory _tmp_ic = new ICategory();
                            boolean canAgg = false;
                            String alias = "";
                            if (_tmp_f != null) {
                                _tmp_ic.setName(_tmp_f.getGroupName());
                                _tmp_ic.setOrder(_tmp_f.getOrder());
                                canAgg = _tmp_f.getAgg().equals("true");
                                alias = _tmp_f.getName();
                            } else {
                                _tmp_ic.setKey(OTHER_FIELD.getKey());
                                _tmp_ic.setName(OTHER_FIELD.getName());
                                _tmp_ic.setOrder(OTHER_FIELD.getOrder());
                            }
                            List<IField> fields = map.get(_tmp_ic);
                            if (fields == null) {
                                fields = new ArrayList<>();
                                map.put(_tmp_ic, fields);
                            }
                            IField _tmp_i_field = new IField(key, alias, (JSONObject) ppEntry.getValue());
                            _tmp_i_field.setAgg(canAgg);
                            fields.add(_tmp_i_field);
                        }
                    }
                }
            }
        }
        return map;
    }

    private static String getFieldPointByType(String type) {
        if (StringUtils.isBlank(type)) {
            return ALL_INDEX_MAPPING_POINT;
        } else {
            if ("search".equals(type)) {
                return ALL_INDEX_MAPPING_POINT;
            } else if ("alert".equals(type)) {
                return ALERT_INDEX_MAPPING_POINT;
            } else if ("scenes".equals(type)) {
//                return SCENES_INDEX_MAPPING_POINT;
            } else if ("incident".equals(type)) {
//                return INCIDENT_INDEX_MAPPING_POINT;
            } else {
//                return ALL_INDEX_MAPPING_POINT;
            }
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.dbapp.cpsysportal.service.ITraceService#hitsByQuery(java.lang.String)
     */
    @Override
    public ESResult hitsByQuery(String query, String path) throws Exception {
        logger.info("hitsByQuery" + " POST" + " " + path + " " + query);
        HttpEntity entity = new NStringEntity(query,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("POST", path, Collections.<String, String>emptyMap(), entity);
        return DataAdapter.json2Result(EntityUtils.toString(resp.getEntity()));
    }

    @Override
    public String updateByQuery(String query, String path, TraceParam traceParam, String index) throws Exception {
        logger.info("updateByQuery" + " POST" + " " + path + " " + query);
        HttpEntity entity = new NStringEntity(query,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("POST", path, Collections.<String, String>emptyMap(), entity);
        JSONArray documents = DataAdapter.jsonUpdate(EntityUtils.toString(resp.getEntity()), traceParam);
        String result = "";
        for (int i = 0; i < documents.size(); i++) {
            JSONObject source = (JSONObject) documents.get(i);
            String _id = source.getString("_id");
            String _type = source.getString("_type");
            result += update(((JSONObject) source.get("_source")).toJSONString(), _type, traceParam.getName(), _id, index) + "\t ;";
        }
        return result;
    }

    @Override
    public String updateByEventIdQuery(String query, String path, String status, String index) throws Exception {
        logger.info("updateByQuery" + " POST" + " " + path + " " + query);
        HttpEntity entity = new NStringEntity(query,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("POST", path, Collections.<String, String>emptyMap(), entity);
        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(resp.getEntity()));
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        String result = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject source = (JSONObject) jsonArray.get(i);
            String uuid = source.getString("_id");
            String _type = source.getString("_type");
            DataAdapter.updatealarmDoc(source, status);
            result += update(((JSONObject) source.get("_source")).toJSONString(), _type, null, uuid, index);
        }
        return result;
    }

    @Override
    public String updateByUUID(String uuid, String type, String index, TraceParam traceParam) throws Exception {
        Response response = client.performRequest("GET", "/" + index + "/" + type + "/" + uuid);
        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        DataAdapter.updateDocument(jsonObject, traceParam);
        String result = "";
        result += update(((JSONObject) jsonObject.get("_source")).toJSONString(), type, traceParam.getName(), uuid, index);
        return result;
    }

    @Override
    public String getByUUID(String uuid, String type, String index, TraceParam traceParam) throws Exception {
        Response response = client.performRequest("GET", "/" + index + "/" + type + "/" + uuid);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String updateByEventId(String uuid, String type, String index, String status) throws Exception {
        Response response = client.performRequest("GET", "/" + index + "/" + type + "/" + uuid);
        JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        DataAdapter.updatealarmDoc(jsonObject, status);
        String result = "";
        result += update(((JSONObject) jsonObject.get("_source")).toJSONString(), type, null, uuid, index);
        return result;
    }

    @Override
    public String insert(String index, String type, String source) throws Exception {
        HttpEntity entity = new NStringEntity(source,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("PUT", "/" + index + "/" + type,
                Collections.<String, String>emptyMap(), entity);
        return EntityUtils.toString(resp.getEntity());
    }

    @Override
    public String delete(String index) throws Exception {
        logger.info("Index should been deleted:");
        Response resp = client.performRequest("DELETE", "/" + index);
        System.out.println("删除 索引" + index + "成功");
        logger.info(" deleted Index" + index);
        return EntityUtils.toString(resp.getEntity());
    }

    @Override
    public boolean indexExists(String index) throws Exception {
        Response resp = client.performRequest("GET", "/" + index);
        JSONObject object = JSONObject.parseObject(EntityUtils.toString(resp.getEntity()));
        return !object.containsKey("error");
    }

    @Override
    public boolean indexClose(String indexes) throws Exception {
        if (!indexes.isEmpty()) {
            String[] index = indexes.split(",");
            for (int i = 0; i < index.length; i++) {
                if (indexExists(index[i])) {
                    Response resp = client.performRequest("POST", "/" + index[i]+"/_close");
                }
            }
            logger.info("关闭 索引" + indexes + "成功");

        }
        return true;
    }

    @Override
    public boolean indexOpen(String indexes) throws Exception {
        String[] index = indexes.split(",");
        for (int i = 0; i < index.length; i++) {
            Response resp = client.performRequest("POST", "/" + index[i]+"/_open");
        }
        return true;
    }

    @Override
    public boolean indexCreate(String index) throws Exception {
        Response resp = client.performRequest("PUT", "/" + index);
        JSONObject object = JSONObject.parseObject(EntityUtils.toString(resp.getEntity()));
        return !object.containsKey("error");
    }

    @Override
    public boolean scrollDelete(String scrollid)throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("scroll_id",scrollid);
        HttpEntity entity = new NStringEntity(jsonObject.toString(),
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("DELETE", "/_search/scroll",Collections.<String, String>emptyMap(), entity);
        JSONObject object = JSONObject.parseObject(EntityUtils.toString(resp.getEntity()));
        return !object.containsKey("error");
    }

    @Override
    public long indexCount(String indexes) throws Exception {
        Response resp = client.performRequest("GET","/"+indexes+ "/_search");
        JSONObject object =  JSONObject.parseObject(EntityUtils.toString(resp.getEntity()));
        if (object.containsKey("hits")) {
            return object.getJSONObject("hits").getLongValue("total");
        }        return 0;
    }

    @Override
    public List<Index> indexList() {
        return CurlUtil.indexList(ip,restPort);
    }

    @Override
    public Map<String, String> getNodeStats()throws Exception {
        Response resp = client.performRequest("GET", "/_nodes/stats");
        return  DataAdapter.json2Map(EntityUtils.toString(resp.getEntity()));
    }

    @Override
    public List<Bucket> getSingleSummary(String query, String field, String typePoint) throws Exception {
        logger.info("POST" + " " + typePoint + "\t" + query);
        HttpEntity entity = new NStringEntity(query,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("POST", typePoint, Collections.<String, String>emptyMap(), entity);
        return DataAdapter.json2SingleField(EntityUtils.toString(resp.getEntity()), field);

    }

    @Override
    public String index_refresh(String index) {
        try {
            return command("POST", "/" + index + "/_refresh");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String query(String query, String method, String path) throws Exception {
        HttpEntity entity = new NStringEntity(query,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        if (path.indexOf("_count") < 0) {
            logger.info(method + " " + path + "\t" + query);
        }
        Response resp = client.performRequest(method, path, Collections.<String, String>emptyMap(), entity);
        return EntityUtils.toString(resp.getEntity());
    }

    @Override
    public String save(String template, String type, String name) throws Exception {
        HttpEntity entity = new NStringEntity(template,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("POST",
                "/.ailpha/" + type + "/" + UUID.randomUUID().toString().replaceAll("-", ""),
                Collections.<String, String>emptyMap(), entity);
        return EntityUtils.toString(resp.getEntity());
    }

    @Override
    public String update(String template, String type, String name, String uuid, String index) throws Exception {
        HttpEntity entity = new NStringEntity(template,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("PUT", "/" + index + "/" + type + "/" + uuid,
                Collections.<String, String>emptyMap(), entity);
        return EntityUtils.toString(resp.getEntity());
    }

    @Override
    public String command(String method, String path) throws Exception {
        return EntityUtils
                .toString(client.performRequest(method, path, Collections.<String, String>emptyMap()).getEntity());
    }

    @Override
    public void refresh(String type) {
//        load();
        fields.remove(type);
        try {
            get_field_by_type(type);
        } catch (Exception e) {
            logger.error("{}", e);
        }
    }

    private long timeDiff(String start, String end) {
        Date s = ElasticDate.parse(start);
        Date e = ElasticDate.parse(end);
        return e.getTime() - s.getTime();
    }

    @Override
    public String getDateHistogramInterval(String start, String end, String originalInterval) {
        String interval;
        long diff = timeDiff(start, end);
        long splits = diff / interval2Long(originalInterval);
        if (splits < 120)
            return originalInterval;
        long oneMinuteTime = 60 * 1000L;
        long oneHourTime = 60 * 60 * 1000L;
        long oneDayTime = 24 * 60 * 60 * 1000L;
        if (diff < oneMinuteTime) {
            interval = "1s";
        } else if (diff < oneHourTime) {
            interval = "30s";
        } else if (diff < 2.5 * oneHourTime) {
            interval = "1m";
        } else if (diff < 7.5 * oneHourTime) {
            interval = "5m";
        } else if (diff < 16.67 * oneHourTime) {
            interval = "10m";
        } else if (diff < 1.5 * oneDayTime) {
            interval = "30m";
        } else if (diff < 3 * oneDayTime) {
            interval = "1h";
        } else if (diff < 7 * oneDayTime) {
            interval = "2h";
        } else if (diff < 15 * oneDayTime) {
            interval = "4h";
        } else if (diff < 24 * oneDayTime) {
            interval = "6h";
        } else if (diff < 31 * oneDayTime) {
            interval = "8h";
        } else if (diff < 62 * oneDayTime) {
            interval = "1d";
        } else if (diff < 180 * oneDayTime) {
            interval = "2d";
        } else if (diff < 2 * 366 * oneDayTime) {
            interval = "1w";
        } else {
            interval = "1M";
        }
        return interval;
    }

    private long interval2Long(String interval) {
        try {
            if (interval == null || "".equals(interval.trim())) {
                return 1L;
            }
            String suffix = interval.substring(interval.length() - 1, interval.length());
            String time = interval.substring(0, interval.length() - 1);
            switch (suffix) {
                case "s":
                    return Integer.parseInt(time) * 1000;
                case "m":
                    return Integer.parseInt(time) * 60 * 1000;
                case "h":
                    return Integer.parseInt(time) * 60 * 60 * 1000;
                case "d":
                    return Integer.parseInt(time) * 24 * 60 * 60 * 1000;
                case "w":
                    return Integer.parseInt(time) * 7 * 24 * 60 * 60 * 1000;
                case "M":
                    return Integer.parseInt(time) * 30 * 24 * 60 * 60 * 1000;
                case "q":
                    return Integer.parseInt(time) * 30 * 24 * 60 * 60 * 1000 * 3;
                case "y":
                    return Integer.parseInt(time) * 365 * 24 * 60 * 60 * 1000;
                default:
                    return 1L;
            }
        } catch (Exception e) {
            logger.error("interval parse error with value {}", interval);
        }
        return 1L;
    }

    @Override
    public Map<String, Object> dateHistogram(String query, String path) throws IOException {
        logger.info("hitsByQuery" + " POST" + " " + path + " " + query);
        HttpEntity entity = new NStringEntity(query,
                ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8")));
        Response resp = client.performRequest("POST", path, Collections.<String, String>emptyMap(), entity);
        return DataAdapter.seriesByDateHistogram(EntityUtils.toString(resp.getEntity()), "yyyy-MM-dd HH:mm:ss");
    }


}
