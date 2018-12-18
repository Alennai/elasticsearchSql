package org.parc.restes.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class ESConstant {
    //	public static final String SIMPLE_SEARCH_POINT = "/xwd-index-*/_search";
//	public static final String ALL_INDEX_MAPPING_POINT = "/xwd-index-*/_mapping";
    public static final String SIMPLE_SEARCH_POINT = "/ailpha-securitylog-*/_search";
    public static final String ALL_INDEX_MAPPING_POINT = "/ailpha-securitylog-*/_mapping";
    public static final String ALERT_INDEX_MAPPING_POINT = "/xwd-securityevent-index/_mapping";
    public static final String SCENES_INDEX_MAPPING_POINT = "/xwd-securityevent-index-agg/_mapping";
    public static final String INCIDENT_INDEX_MAPPING_POINT = "/xwd-securityevent-incident/_mapping";

    /**
     *
     */
    public static final String onlyIp = "onlyIp";
    public static final String lowAddress = "lowAddress";
    public static final String highAddress = "highAddress";
    public static final String type_idc = "idc";
    public static final String type_scan201610 = "scan201610";
    public static final String type_scan20170312 = "scan20170312";
    public static final String type_record_information = "record_information";

    public static final int IP_LIMIT = 5;

    public static final String SECURITYEVENT_INDEX = "/xwd-securityevent-index/_search";
    // public static final String SECURITYEVENT_INDEX =
    // "/xwd-securityevent-index/securityevent/_search";

    public static final String ATTACK_ANALYSIS = "攻击分析";
    public static final String ATTACK_TYPE = "攻击类型";
    public static final String ATTACK_TARGET_INFO = "攻击对象信息";
    public static final String ATTACK_PROOF = "举证";
    // INDEX
    public static final String IP_REPOSITORY_SEARCH = "/iprepository/%s/_search";
    public static final String IP_REPOSITORY_SEARCH_ALL_TYPE = "/iprepository/_search";
    public static final String COUNT_IP_REPOSITORY_SEARCH_ALL_TYPE = "/iprepository/_count";
    public static final String ip_similarity = "/ip_similarity/_search";
    public static final String security_event_index = "/xwd-securityevent-index/securityevent/_search";
    public static final String security_event_index_agg = "/xwd-securityevent-index-agg/_search";
    public static final String security_event_index_all_type = "/xwd-securityevent-index/_search";
    //	public static final String xwd_index_all = "/xwd-index-*/_search";
    public static final String xwd_index_all = "/ailpha-securitylog-*/_search";
    public static final String LOG_INDIES = "ailpha-securitylog-*";
    public static final String SECURITY_EVENT = "ailpha-securityevent";
    public static final String SECURITY_ALARM = "ailpha-securityalarm";
    public static final String MSG = "%s 对 %s 进行 [%s]";
    public static final String MSG2 = "%s 针对 %s 存在 [%s]";
    //index manage
//	public final static Pattern ptn1 = Pattern.compile("xwd-index-2\\d{7}");
    public final static Pattern ptn1 = Pattern.compile("ailpha-securitylog-2\\d{7}");
    public static final String TYPE_SEARCH = "/_search";
    public static final String TYPE_COUNT = "/_count";
    public static final String TYPE_DELETE = "/_delete";
    public static final String TYPE_INSERT = "/_insert";
    public static DecimalFormat df = new DecimalFormat("#.00");
}
