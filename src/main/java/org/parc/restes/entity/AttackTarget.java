/** 
 *
 * @author shaco.zhu
 * Date:2017年5月25日下午1:31:35 
 * 
 */
package org.parc.restes.entity;

import java.util.List;

public class AttackTarget {
	private String target;
	private List<Bucket> buckets;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<Bucket> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<Bucket> buckets) {
		this.buckets = buckets;
	}

}
