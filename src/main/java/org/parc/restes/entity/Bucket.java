/** 
 *
 * @author shaco.zhu
 * Date:2017年5月25日下午2:02:08 
 * 
 */
package org.parc.restes.entity;

import java.util.ArrayList;
import java.util.List;

import com.dbapp.cpsysportal.cache.DictionaryCache;

public class Bucket {
	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	private String key;
	private int docCount;
	private Integer total;
	private String percentage;
	private String name;
	private List<Bucket> subs;
	private Integer score;
	private String value;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 */
	public Bucket() {
		super();
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the docCount
	 */
	public int getDocCount() {
		return docCount;
	}

	/**
	 * @param docCount
	 *            the docCount to set
	 */
	public void setDocCount(int docCount) {
		this.docCount = docCount;
	}

	/**
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<Bucket> getSubs() {
		return subs;
	}

	public void setSubs(List<Bucket> subs) {
		this.subs = subs;
	}

	/**
	 * @param key
	 * @param docCount
	 * @param total
	 */
	public Bucket(String field, String key, int docCount, Integer total) {
		super();
		this.key = key;
		this.name = DictionaryCache.fieldByCategory(field, key);
		this.docCount = docCount;
		this.value = docCount + "";
		this.total = total;
		this.percentage = String.format("%.1f", docCount / (total * 1.0) * 100);
	}

	/**
	 * @param key
	 * @param docCount
	 */
	public Bucket(String field, String key, int docCount) {
		super();
		this.field = field;
		this.key = key;
		this.docCount = docCount;
		this.name = DictionaryCache.fieldByCategory(field, key);
	}

	/**
	 * @return the percentage
	 */
	public String getPercentage() {
		return percentage;
	}

	public void addBucket(Bucket bucket) {
		if (subs == null) {
			subs = new ArrayList<>();
		}
		subs.add(bucket);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
