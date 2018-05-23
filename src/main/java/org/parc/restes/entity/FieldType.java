/**
 * @author shaco.zhu
 * @email shaco.zhu@dbappsecurity.com.cn
 * Date:2017年7月25日
 */
package org.parc.restes.entity;

public enum FieldType {
	search("search"), scenes("scenes"), alert("alert");
	private String type;

	private FieldType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
