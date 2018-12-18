package org.parc.restes.service;


import org.parc.restes.entity.Bucket;
import org.parc.restes.entity.ESResult;
import org.parc.restes.entity.Field;
import org.parc.restes.entity.Index;
import org.parc.restes.entity.TraceParam;

import java.util.List;
import java.util.Map;

public interface IEsService {
    Field categories(String type, String fieldName);

    List<Map<String, Object>> get_field_by_type(String type) throws Exception;

    ESResult hitsByQuery(String query, String path) throws Exception;

    List<Bucket> getSingleSummary(String query, String field, String typePoint) throws Exception;

    String index_refresh(String index);

    String query(String query, String method, String path) throws Exception;

    String save(String template, String type, String name) throws Exception;

    String update(String template, String type, String name, String uuid, String index) throws Exception;

    String command(String method, String path) throws Exception;

    void refresh(String type);

    String getDateHistogramInterval(String start, String end, String originalInterval);

    Map<String, Object> dateHistogram(String query, String path) throws Exception;

    String updateByEventIdQuery(String query, String path, String status, String index) throws Exception;

    String updateByQuery(String query, String path, TraceParam traceParam, String index) throws Exception;

    String updateByUUID(String uuid, String type, String index, TraceParam traceParam) throws Exception;

    String getByUUID(String uuid, String type, String index, TraceParam traceParam) throws Exception;

    String updateByEventId(String uuid, String type, String index, String status) throws Exception;

    String insert(String index, String type, String source) throws Exception;

    String delete(String index) throws Exception;

    boolean indexExists(String index) throws Exception;

    boolean indexClose(String indexes) throws Exception;

    boolean indexOpen(String indexes) throws Exception;

    boolean indexCreate(String index) throws Exception;

    boolean scrollDelete(String scrollid) throws Exception;

    long indexCount(String indexes) throws Exception;

    List<Index> indexList();

    Map<String, String> getNodeStats() throws Exception;

    default String compositePath(String[] indexes, String[] types, String opt) {
        if (types == null) {
            return "/" + String.join(",", indexes) + "/" + opt;
        }
        return "/" + String.join(",", indexes) + "/" + String.join(",", types) + "/" + opt;
    }

}
