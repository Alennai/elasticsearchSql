/**
 *
 * @author shaco.zhu
 * Date:2017年5月25日下午3:03:33 
 * eg : restParamsBuilder().size(0).must("srcAddress", "127.0.0.1").aggTerms("srcAddress", "5").json()
 */
package org.parc.restes.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.parc.restes.entity.DateUtil;
import org.parc.restes.entity.Param;
import org.parc.restes.entity.TraceParam;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class RestParamsBuilder {
	private XContentBuilder xContentBuilder;
	private List<Map<String, Object>> shoulds = new ArrayList<>();
	private List<Map<String, Object>> musts = new ArrayList<>();
	private List<Map<String, Object>> mustNots = new ArrayList<>();
	private List<Map<String, Map<String, Object>>> aggs = new ArrayList<>();
	private List<Map<String, Object>> orders = new ArrayList<>();
	private List<Map<String, Object>> filtermust = new ArrayList<>();
	private List<Map<String, Object>> filtershould = new ArrayList<>();
	private HashSet<String> sourceFilter = new HashSet<>();

	private Map<String, String> analyzer_fields = new HashMap<String, String>(){{
		put("rawEvent","");
		put("message","");
		put("chineseModelName","");
		put("name","");
	}};
	private RestParamsBuilder() {
		try {
			xContentBuilder = jsonBuilder().startObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static RestParamsBuilder restParamsBuilder() {
		return new RestParamsBuilder();
	}

	private RestParamsBuilder must(String k, String v) {
		musts.add(term("term", term(k, v)));
		return this;
	}

	public RestParamsBuilder should(String k, String v) {
		shoulds.add(term("term", term(k, v)));
		return this;
	}

	private RestParamsBuilder match(String k, String v) {
		musts.add(term("match", matchTerm(k, v)));
		return this;
	}

	public RestParamsBuilder should(String k, List<String> v) {
		shoulds.add(term("terms", term(k, v)));
		return this;
	}

	public RestParamsBuilder moutNot(String k, String v) {
		mustNots.add(term("term", term(k, v)));
		return this;
	}

	public RestParamsBuilder must(String k, String[] v) {
		musts.add(term("terms", term(k, v)));
		return this;
	}

	private RestParamsBuilder must(String k, List<String> v) {
		musts.add(term("terms", term(k, v)));
		return this;
	}

	private RestParamsBuilder match(String k, List<String> v) {
		StringBuilder sb = new StringBuilder();
		for(String value : v){
			sb.append(value).append(" ");
//            musts.add(term("match", matchTerm(k, value)));
		}
		musts.add(term("match", matchTerm(k, sb.toString().trim())));
		return this;
	}
	private RestParamsBuilder mul_match(String keyword, List<String> fields, String operator) {
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("query", keyword);
		fieldMap.put("operator", operator);
		fieldMap.put("fields", fields);
		Map<String, Object> multi_match = new HashMap<>();
		multi_match.put("multi_match", fieldMap);
		musts.add(multi_match);
		return this;
	}

	public RestParamsBuilder matchPhrase(String k, String v) {
		Map<String, Object> match = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("query", v);
		match.put(k, data);
		return this;
	}

	private RestParamsBuilder must(String k, Object lt, Object gt) {
		Map<String, Object> range = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("gte", lt);
		data.put("lte", gt);
		range.put(k, data);
		musts.add(term("range", range));
		return this;
	}

	public RestParamsBuilder rangeUnilateral(String k, Object v, String symbol) {
		Map<String, Object> range = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put(symbol, v);
		range.put(k, data);
		musts.add(term("range", range));
		return this;
	}

	public RestParamsBuilder explain() throws IOException {
		xContentBuilder.field("explain", true);
		return this;
	}

	private Map<String, Object> matchTerm(String k, String v){
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("query", v);
		fieldMap.put("operator", "or");
		Map<String, Object> tmp = new HashMap<>();
		tmp.put(k, fieldMap);
		return tmp;
	}

	private Map<String, Object> term(String k, Object v) {
		Map<String, Object> tmp = new HashMap<>();
		tmp.put(k, v);
		return tmp;
	}

	public RestParamsBuilder all() throws IOException {
		xContentBuilder.field("query", ImmutableMap.of("match_all", new HashMap<>()));
		return this;
	}

	public String json() throws IOException {
		if (!orders.isEmpty()) {
			xContentBuilder.field("sort", orders);
		}
		Map<String, Object> filterbool = new HashMap<>();
		Map<String, Object> bool = new HashMap<>();
		if(!sourceFilter.isEmpty()){
			xContentBuilder.field("_source", sourceFilter);
		}
		if (!musts.isEmpty())
			bool.put("must", musts);
		if (!shoulds.isEmpty())
			bool.put("should", shoulds);
		if (!mustNots.isEmpty())
			bool.put("must_not", mustNots);
		if (!bool.isEmpty())
			xContentBuilder.field("query", term("bool", bool));
		if (!aggs.isEmpty()) {
			Map<String, Map<String, Map<String, Object>>> currentTmp = null;
			Object finalObj = null;
			int size = aggs.size();
			for (int i = size - 1; i >= 0; i--) {
				Map<String, Map<String, Object>> tmp = aggs.get(i);
				if (currentTmp == null) {
					currentTmp = new HashMap<>();
					currentTmp.put("aggs", tmp);
				} else {
					String tk = "";
					Iterator<String> itk = tmp.keySet().iterator();
					while (itk.hasNext()) {
						tk = itk.next();
					}
					tmp.get(tk).putAll(currentTmp);
					currentTmp.put("aggs", tmp);
				}
				if (i == 0) {
					finalObj = tmp;
				} else {
					finalObj = ImmutableMap.of("aggs", tmp);
				}
			}
			xContentBuilder.field("aggs", finalObj);
		}
//		if (!filtermust.isEmpty()) {
//			filterbool.put("must", filtermust);
//		}
//		if (!filtershould.isEmpty()) {
//			filterbool.put("should", filtershould);
//		}
//		if(filterbool.size() > 0){
//			xContentBuilder.field("filter", term("bool", filterbool));
//		}
		return xContentBuilder.endObject().string();
	}

	private RestParamsBuilder from(int from) throws IOException {
		xContentBuilder.field("from", from);
		return this;
	}

	private RestParamsBuilder size(int size) throws IOException {
		xContentBuilder.field("size", size);
		return this;
	}

	public RestParamsBuilder source(String source){
		sourceFilter.add(source);
		return this;
	}

	public RestParamsBuilder source(List<String> sources){
		sourceFilter.addAll(sources);
		return this;
	}

	private RestParamsBuilder order(String k, String order, String type) {
		Map<String, Object> sort = new HashMap<>();
		Map<String, String> data = null;
		if (type != null) {
			data = ImmutableMap.of("order", order, "unmapped_type", type);
		} else {
			data = ImmutableMap.of("order", order);
		}
		sort.put(k, data);
		orders.add(sort);
		return this;
	}

	public RestParamsBuilder order(String k) throws IOException {
		return order(k, "desc", "long");
	}

	public RestParamsBuilder order(String k, String order) throws IOException {
		return order(k, order, null);
	}

	public RestParamsBuilder order() throws IOException {
		return order("evnetId", "desc", "long");
	}

	public RestParamsBuilder stored_fields(String... argv) throws IOException {
		xContentBuilder.field("stored_fields", argv);
		return this;
	}

	private RestParamsBuilder queryString(String query, String default_field) {
		if (StringUtils.isBlank(query))
			return this;
		Map<String, Object> queryString = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		data.put("default_field", default_field);
		data.put("query", query);
		queryString.put("query_string", data);
		musts.add(queryString);
		return this;
	}

	private RestParamsBuilder conditions(List<String> conditions, String default_field) {
		CleverListMap maps = new CleverListMap();
		for (int i = 0; i < conditions.size(); i++) {
			String condition = conditions.get(i);
			if (StringUtils.isBlank(condition)) {
				continue;
			}
			int pos = condition.indexOf(":");
			if (pos > 0) {
				String k = condition.substring(0, pos);
				String v = condition.substring(pos + 1, condition.length());
				if ("@timestamp".equals(k)){
					this.timeRange("@timestamp", DateUtil.str2Long(v) - 1,DateUtil.str2Long(v) + 999);
					continue;
				}
				if ("uuid".equals(k)){
					k = "_id";
				}
				//存在srcGeoPoint字段，取后一值
				if ("srcGeoPoint".equals(k) && conditions.get(i+1).indexOf(":") < 0){
					v = v + "," + conditions.get(++i);
				}
//				v = org.apache.lucene.queryparser.classic.QueryParser.escape(v);
				maps.putSingle(k, v);
			} else {
				maps.putSingle(default_field, condition);
			}
		}
		for (Entry<String, List<String>> entry : maps.entrySet()) {
			String k = entry.getKey();
			List<String> v = entry.getValue();
			if (v.size() == 0) {
				continue;
			} else if (v.size() == 1) {
				if(analyzer_fields.containsKey(k)){
					this.match(k, v.get(0));
				}else{
					this.must(k, v.get(0));
				}
			} else {
				if(analyzer_fields.containsKey(k)){
					this.match(k, v);
				}else{
					this.must(k, v);
				}
			}
		}
		return this;
	}

	public RestParamsBuilder keywords(List<String> keywords,String default_field) {
		CleverListMap maps = new CleverListMap();
		for (String keyword : keywords) {
			if (StringUtils.isBlank(keyword)) {
				continue;
			}
			int pos = keyword.indexOf(":");
			if (pos > 0) {
				String k = keyword.substring(0, pos);
				String v = keyword.substring(pos + 1, keyword.length());
				maps.putSingle(k, v);
			} else {
				maps.putSingle(default_field, keyword);
			}
		}
		for (Entry<String, List<String>> entry : maps.entrySet()) {
			String k = entry.getKey();
			List<String> v = entry.getValue();
			if (v.size() == 0) {
				continue;
			} else if (v.size() == 1) {
				if(analyzer_fields.containsKey(k)){
					this.match(k, v.get(0));
				}else{
					this.must(k, v.get(0));
				}
			} else {
				if(analyzer_fields.containsKey(k)){
					this.match(k, v);
				}else{
					this.must(k, v);
				}
			}
		}
		return this;
	}

	public RestParamsBuilder keywordmap(Map keywords,String default_field) {
		CleverListMap maps = new CleverListMap();
		for (Object keyword : keywords.keySet()) {
			if (StringUtils.isBlank(keyword.toString())) {
				continue;
			}
			List<String> keywordslist = (List<String>) keywords.get(keyword);
			List<String> fields = new ArrayList<>();
			for(String field : keywordslist){
				if(analyzer_fields.containsKey(field)){
					fields.add(field);
				}
			}
			this.mul_match(keyword.toString(), fields, "or");
		}
		return this;
	}

	private RestParamsBuilder aggTerms(String field, int size) {
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("field", field);
		tmp.put("size", size);
		tmp.put("order", defaultAggOrder());
		Map<String, Object> terms = new HashMap<>();
		terms.put("terms", tmp);
		aggs.add(ImmutableMap.of(field, terms));
		return this;
	}

	public RestParamsBuilder aggDateHistogram(String field, String interval) {
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("field", field);
		tmp.put("interval", interval);
		Map<String, Object> terms = new HashMap<>();
		terms.put("date_histogram", tmp);
		aggs.add(ImmutableMap.of(field, terms));
		return this;
	}

	public RestParamsBuilder aggDateHistogram(String field, String interval, String timeZone) {
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("field", field);
		tmp.put("interval", interval);
		tmp.put("time_zone", timeZone);
		tmp.put("min_doc_count", 0);
		Map<String, Object> terms = new HashMap<>();
		terms.put("date_histogram", tmp);
		aggs.add(ImmutableMap.of(field, terms));
		return this;
	}

	public RestParamsBuilder aggDateHistogram(String field, String interval, String timeZone, String min,
											  String max) {
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("field", field);
		tmp.put("interval", interval);
		tmp.put("time_zone", timeZone);
		tmp.put("min_doc_count", 0);
		Map<String, String> extended_bounds = new HashMap<>();
		extended_bounds.put("min", min);
		extended_bounds.put("max", max);
		tmp.put("extended_bounds", extended_bounds);
		Map<String, Object> terms = new HashMap<>();
		terms.put("date_histogram", tmp);
		aggs.add(ImmutableMap.of(field, terms));
		return this;
	}

	public RestParamsBuilder aggDateHistogram(String field, String interval, String timeZone, String format, long max,
											  long min) {
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("field", field);
		tmp.put("interval", interval);
		tmp.put("format", format);
		tmp.put("time_zone", timeZone);
		Map<String, Long> extended_bounds = new HashMap<>();
		extended_bounds.put("min", min);
		extended_bounds.put("max", max);
		tmp.put("extended_bounds", extended_bounds);
		Map<String, Object> terms = new HashMap<>();
		terms.put("date_histogram", tmp);
		aggs.add(ImmutableMap.of(field, terms));
		return this;
	}

	private Map<String, Object> defaultAggOrder() {
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("_count", "desc");
		return tmp;
	}

	public static String buildRestJsonByTemplate(TraceParam traceParam) {
		return String.format(traceParam.getTemplate(), getTimeLong(traceParam.getStart()),
				getTimeLong(traceParam.getEnd()));
	}

	public static RestParamsBuilder buildRestJson(TraceParam traceParam) throws IOException {
		return buildRestJson(traceParam, false, false);
	}

	private static RestParamsBuilder buildRestJson(TraceParam traceParam, boolean isTemplate, boolean noTime)
			throws IOException {
		RestParamsBuilder builder = restParamsBuilder();
		List<Param> ps = traceParam.getParams();
		if (ps != null)
			for (Param p : ps) {
				builder.aggTerms(p.getKey(), p.getSize());
			}
		if (!noTime) {
			if (isTemplate) {
				builder.timeRangeTemplate("@timestamp");
			} else {
				String start = traceParam.getStart();
				String end = traceParam.getEnd();
				if (start != null && end != null)
					builder.timeRange("@timestamp", traceParam.getStart(), traceParam.getEnd());
			}
			List<String> sorts = traceParam.getSorts();
			if (sorts != null) {
				builder.order("@timestamp", "desc", null);
			}
		}
		String query = traceParam.getQuery();
		if (StringUtils.isNotBlank(query)) {
			builder.queryString(query,getDefaultFieldByType(traceParam.getSaveType()));
		}
		List<String> conditions = traceParam.getConditions();
		if (conditions != null && !conditions.isEmpty()) {
			builder.conditions(conditions,getDefaultFieldByType(traceParam.getSaveType()));
		}
		int from = traceParam.getFrom();
		if (from > 0)
			builder.from(from);
		int size = traceParam.getSize();
		if (size>0)
			builder.size(size);
		return builder;
	}

	private static String getDefaultFieldByType(String saveType) {
		String default_field="rawEvent";
		if (saveType.equals("incident")){
			default_field="keywords";
		}
		return default_field;
	}

	private static long getTimeLong(String time) {
		try {
			return CDateUtil.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private RestParamsBuilder timeRange(String key, String start, String end) {
		must(key, getTimeLong(start), getTimeLong(end));
		return this;
	}

	public RestParamsBuilder timeRange(String key, Date start, Date end) {
		must(key, start.getTime(), end.getTime());
		return this;
	}

	private RestParamsBuilder timeRange(String key, long start, long end) {
		must(key, start, end);
		return this;
	}

	private RestParamsBuilder timeRangeTemplate(String key) {
		must(key, "%s", "%s");
		return this;
	}

	public static String savedQuery(String template, String start, String end) {
		return String.format(template, getTimeLong(start), getTimeLong(end));
	}

	public RestParamsBuilder fieldRange(String key, int start, int end) {
		must(key, start, end);
		return this;
	}

	public RestParamsBuilder fieldRanges(List<Map> ranges) throws IOException {
		for( Map temp : ranges){
			for(Object key : temp.keySet()){
				JSONArray fromTo = (JSONArray)temp.get(key.toString());
				List<Map<String, Object>> localshoulds = new ArrayList<>();
				if(fromTo.size() > 1){
					for(Object single : fromTo){
						int start = ((JSONObject)single).getInteger("from");
						int end = ((JSONObject)single).getInteger("to");
						Map<String, Object> data = new HashMap<>();
						Map<String, Object> range = new HashMap<>();
						data.put("gte", start);
						data.put("lte", end);
						range.put(key.toString(), data);
						Map<String, Object> rangeshould = new HashMap<>();
						rangeshould.put("range", range);
//						filtershould.add(rangeshould);
						shoulds.add(rangeshould);
//						localshoulds.add(range);
					}
					filterShould(localshoulds);
				}else if(fromTo.size() == 1){
					JSONObject single = (JSONObject) fromTo.toArray()[0];
					int start = single.getInteger("from");
					int end = single.getInteger("to");
					filterMust(key.toString(), start, end);
					Map<String, Object> data = new HashMap<>();
					Map<String, Object> range = new HashMap<>();
					Map<String, Object> rangemust = new HashMap<>();
					data.put("gte", start);
					data.put("lte", end);
					range.put(key.toString(), data);
					rangemust.put("range", range);
					musts.add(rangemust);
				}
			}
		}
		return this;
	}

	private void filterShould(List<Map<String, Object>> localshoulds) {
		Map<String, Object> ranges = new HashMap<>();
		ranges.put("range", localshoulds);
		filtershould.add(ranges);
	}

	private RestParamsBuilder filterMust(String k, Object lt, Object gt) {
		Map<String, Object> data = new HashMap<>();
		Map<String, Object> range = new HashMap<>();
		Map<String, Object> ranges = new HashMap<>();
		data.put("gte", lt);
		data.put("lte", gt);
		range.put(k, data);
		ranges.put("range", range);
		filtermust.add(ranges);
		return this;
	}

	public RestParamsBuilder fuzzy(String words, ArrayList<String> field) {
		Map<String, Object> multimatchs = new HashMap<>();
		Map<String, Object> fuzzy = new HashMap<>();
		fuzzy.put("fields", field);
		fuzzy.put("fuzziness", "AUTO");
		fuzzy.put("query", words);
		multimatchs.put("multi_match", fuzzy);
		musts.add(multimatchs);
		return this;
	}


	public RestParamsBuilder enumCondition(List<Set> lists) {
		for(Set<String> list : lists) {
			Map<String, String> maps = new HashMap<>();
			List<Map<String, Object>> localshoulds = new ArrayList<>();
			for (String condition : list) {
				if (StringUtils.isBlank(condition)) {
					continue;
				}
				int pos = condition.indexOf(":");
				if (pos > 0) {
					String k = condition.substring(0, pos);
					String v = condition.substring(pos + 1, condition.length());
					maps.put(v, k);
				}
			}
			for (String key : maps.keySet()) {
                String k = maps.get(key);
				if (StringUtils.isBlank(key)) {
					continue;
				} else {
					localshoulds.add(term("term", term(k, key)));
				}
			}
			musts.add(term("bool", term("should", localshoulds)));
		}
		return this;
	}

	public RestParamsBuilder conditionSet(List<Set> conditions) {
		for (Set<String> list : conditions) {
			Map<String, String> maps = new HashMap<>();
			List<Map<String, Object>> localshoulds = new ArrayList<>();
			for (String condition : list) {
				if (StringUtils.isBlank(condition)) {
					continue;
				}
				int pos = condition.indexOf(":");
				if (pos > 0) {
					String k = condition.substring(0, pos);
					String v = condition.substring(pos + 1, condition.length());
					maps.put(v, k);
				}
			}
			if(maps.size() > 1){
				for (String key : maps.keySet()) {
                    String k = maps.get(key);
					if (StringUtils.isBlank(key)) {
						continue;
					} else {
						if (analyzer_fields.containsKey(k)) {
							localshoulds.add(term("match", term(k, key)));
						} else {
							localshoulds.add(term("term", term(k, key)));
						}

					}

				}
				musts.add(term("bool", term("should", localshoulds)));
			}else if(maps.size() == 1){
				for (String key : maps.keySet()) {
                    String k = maps.get(key);
					if (StringUtils.isBlank(key)) {
						continue;
					} else {
						if (analyzer_fields.containsKey(k)) {
							this.match(k, key);
						} else {
							this.must(k, key);
						}
					}
				}
			}
		}
		return this;
	}
}
