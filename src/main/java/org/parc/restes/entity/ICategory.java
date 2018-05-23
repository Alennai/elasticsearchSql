/**
 * @author shaco.zhu
 * @email shaco.zhu@dbappsecurity.com.cn
 * Date:2017年7月25日
 */
package org.parc.restes.entity;


import org.apache.commons.lang3.StringUtils;

public class ICategory {
	private String key;
	private String name;
	private Integer order;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @param key
	 * @param name
	 * @param order
	 */
	public ICategory(String key, String name, Integer order) {
		super();
		this.key = key;
		this.name = name;
		this.order = order;
	}

	/**
	 * 
	 */
	public ICategory() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ICategory) {
			ICategory ic = (ICategory) obj;
			if (StringUtils.isBlank(name)) {
				return false;
			}
			if (ic.getOrder() == this.order && ic.getName().equals(this.name)) {
				return true;
			} else {
				return false;
			}
		} else
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if (StringUtils.isNotBlank(name))
			return order + name.hashCode() * 100;
		return order;
	}

}
