/** 
 *
 * @author shaco.zhu
 * Date:2017年5月26日上午10:25:06 
 * 
 */
package org.parc.restes.entity;

public class AttackerTraceParam {
//	@ApiParam("开始时间 eg:2017-05-27 00:00:00")
	private String startTime;
//	@QueryParam("endTime")
//	@ApiParam("结束时间 eg:2017-05-28 00:00:00")
	private String endTime;
//	@QueryParam("ip")
//	@ApiParam("only support ipv4")
	private String ip;
//	@QueryParam("timeStr")
//	@ApiParam("当日0d 最近7天7d 本月0m 最近3个月3m 本周0w 最近一周 1w")
	private String timeStr;
	private int ipSize = 10;
	private int hostTop = 1;
	private int warningTypeTop = 3;
	private int proofTop = 3;
	private int gangSize = 3;
	private int proofSize = 20;
	private int defaultSize = 20;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getHostTop() {
		return hostTop == 0 ? 20 : hostTop;
	}

	public void setHostTop(int hostTop) {
		this.hostTop = hostTop;
	}

	public int getWarningTypeTop() {
		return warningTypeTop == 0 ? 20 : warningTypeTop;
	}

	public void setWarningTypeTop(int warningTypeTop) {
		this.warningTypeTop = warningTypeTop;
	}

	public int getProofTop() {
		return proofTop == 0 ? 3 : proofTop;
	}

	public void setProofTop(int proofTop) {
		this.proofTop = proofTop;
	}

	public int getIpSize() {
		return ipSize == 0 ? 10 : ipSize;
	}

	public void setIpSize(int ipSize) {
		this.ipSize = ipSize;
	}

	public int getGangSize() {
		return gangSize == 0 ? 3 : gangSize;
	}

	public void setGangSize(int gangSize) {
		this.gangSize = gangSize;
	}

	public int getProofSize() {
		return proofSize == 0 ? 20 : proofSize;
	}

	public int getDefaultSize() {
		return defaultSize == 0 ? 20 : defaultSize;
	}

	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public void setProofSize(int proofSize) {
		this.proofSize = proofSize;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String toKey() {
		return ip + " _" + timeStr;
	}

	public String toKeyAsset(){
		return ip + "_" + startTime+"_"+endTime;
	}
}
