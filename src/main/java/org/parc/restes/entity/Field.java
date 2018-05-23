
package org.parc.restes.entity;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "field")
public class Field {
	@XmlAttribute(name = "key", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String key;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "agg", required = true)
    protected String agg;
    @XmlAttribute(name = "order", required = false)
    private Integer order;
    @XmlAttribute(name = "groupName", required = false)
	private String groupName;
	
    public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

    /**
     * 获取key属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置key属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * 获取name属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

	public String getAgg() {
		return agg;
	}

	public void setAgg(String agg) {
		this.agg = agg;
	}

}
