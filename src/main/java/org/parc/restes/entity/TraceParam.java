/** 
 *
 * @author shaco.zhu
 * Date:2017年5月26日上午10:25:06 
 * 
 */
package org.parc.restes.entity;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.QueryParam;
import java.util.List;

public class TraceParam {
	@ApiParam("保存类型:[search,scenes,alert,incident]")
	private String saveType;
	@QueryParam("start")
	@ApiParam("开始时间 eg:2017-05-27 00:00:00")
	private String start;
	@QueryParam("end")
	@ApiParam("结束时间 eg:2017-05-28 00:00:00")
	private String end;
	@ApiParam("保存名称")
	private String name;
	@ApiParam("保存列名")
	private List<String> columns;
	@ApiParam("聚合参数")
	private List<Param> params;
	private int size;
	private int from;
	@ApiParam("query模版")
	private String template;
	private List<String> sorts;
	@ApiParam("聚合字段")
	private String field;
	@ApiParam("条件数组")
	private List<String> conditions;
	@QueryParam("uuid")
	@ApiParam("UUID")
	private String uuid;
	@QueryParam("processingMethod")
	@ApiParam("处理方式")
	private String processingMethod;
	@QueryParam("handlingComments")
	@ApiParam("处理意见")
	private String handlingComments;
	@QueryParam("type")
	@ApiParam("es内置分组")
	private String type;
	@QueryParam("falsePositives")
	@ApiParam("是否误报")
	private String falsePositives;
	@ApiParam("时间轴跨度")
	private String interval;

	public String getFalsePositives() {
		return falsePositives;
	}

	public void setFalsePositives(String falsePositives) {
		this.falsePositives = falsePositives;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProcessingMethod() {
		return processingMethod;
	}

	public void setProcessingMethod(String processingMethod) {
		this.processingMethod = processingMethod;
	}

	public String getHandlingComments() {
		return handlingComments;
	}

	public void setHandlingComments(String handlingComments) {
		this.handlingComments = handlingComments;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public List<String> getSorts() {
		return sorts;
	}

	public void setSorts(List<String> sorts) {
		this.sorts = sorts;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	private String query;

	/**
	 * @return the params
	 */
	public List<Param> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<Param> params) {
		this.params = params;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TraceParam [params=" + params + ", query=" + query + "]";
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCstring() {
		if (columns != null && !columns.isEmpty()) {
			return JSON.toJSONString(columns);
		} else {
			return "*";
		}
	}

	public String getCsvCs() {
		if (columns != null && !columns.isEmpty()) {
			return StringUtils.join(columns, ",");
		} else {
			return "@timestamp,rawEvent";
		}
	}

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public void setConditions(List<String> conditions) {
		this.conditions = conditions;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}
}
