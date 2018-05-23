
package org.parc.restes.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="key" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="accessAgent"/>
 *             &lt;enumeration value="appProtocol"/>
 *             &lt;enumeration value="assetCriticality"/>
 *             &lt;enumeration value="bytesIn"/>
 *             &lt;enumeration value="bytesOut"/>
 *             &lt;enumeration value="catAttackFashion"/>
 *             &lt;enumeration value="catAttackSpeed"/>
 *             &lt;enumeration value="catAttackSubtype"/>
 *             &lt;enumeration value="catAttackThroughout"/>
 *             &lt;enumeration value="catAttackType"/>
 *             &lt;enumeration value="catBehavior"/>
 *             &lt;enumeration value="catObject"/>
 *             &lt;enumeration value="catOutcome"/>
 *             &lt;enumeration value="catSignificance"/>
 *             &lt;enumeration value="catTechnique"/>
 *             &lt;enumeration value="clientOperatingSystem"/>
 *             &lt;enumeration value="collectorId"/>
 *             &lt;enumeration value="collectorReceiptTime"/>
 *             &lt;enumeration value="customerId"/>
 *             &lt;enumeration value="destAddress"/>
 *             &lt;enumeration value="destAssetId"/>
 *             &lt;enumeration value="destDnsDomain"/>
 *             &lt;enumeration value="destGeoAddress"/>
 *             &lt;enumeration value="destGeoCity"/>
 *             &lt;enumeration value="destGeoCountry"/>
 *             &lt;enumeration value="destGeoCountryCode"/>
 *             &lt;enumeration value="destGeoCounty"/>
 *             &lt;enumeration value="destGeoId"/>
 *             &lt;enumeration value="destGeoIsp"/>
 *             &lt;enumeration value="destGeoLatitude"/>
 *             &lt;enumeration value="destGeoLongitude"/>
 *             &lt;enumeration value="destGeoPostalCode"/>
 *             &lt;enumeration value="destGeoRegion"/>
 *             &lt;enumeration value="destGeoRegionCode"/>
 *             &lt;enumeration value="destHostName"/>
 *             &lt;enumeration value="destMacAddress"/>
 *             &lt;enumeration value="destNtDomain"/>
 *             &lt;enumeration value="destPort"/>
 *             &lt;enumeration value="destProcessName"/>
 *             &lt;enumeration value="destServiceName"/>
 *             &lt;enumeration value="destTransAddress"/>
 *             &lt;enumeration value="destTransPort"/>
 *             &lt;enumeration value="destTransZone"/>
 *             &lt;enumeration value="destUserId"/>
 *             &lt;enumeration value="destUserName"/>
 *             &lt;enumeration value="destUserPrivileges"/>
 *             &lt;enumeration value="destZone"/>
 *             &lt;enumeration value="deviceAddress"/>
 *             &lt;enumeration value="deviceAssetSubType"/>
 *             &lt;enumeration value="deviceAssetSubTypeId"/>
 *             &lt;enumeration value="deviceAssetType"/>
 *             &lt;enumeration value="deviceAssetTypeId"/>
 *             &lt;enumeration value="deviceCat"/>
 *             &lt;enumeration value="deviceHostname"/>
 *             &lt;enumeration value="deviceId"/>
 *             &lt;enumeration value="deviceModel"/>
 *             &lt;enumeration value="deviceName"/>
 *             &lt;enumeration value="deviceProtocol"/>
 *             &lt;enumeration value="deviceReceiptTime"/>
 *             &lt;enumeration value="deviceVendor"/>
 *             &lt;enumeration value="deviceVersion"/>
 *             &lt;enumeration value="dirname"/>
 *             &lt;enumeration value="dvcAction"/>
 *             &lt;enumeration value="dvcDirection"/>
 *             &lt;enumeration value="dvcFacility"/>
 *             &lt;enumeration value="dvcInInterface"/>
 *             &lt;enumeration value="dvcOutInterface"/>
 *             &lt;enumeration value="dvcPayload"/>
 *             &lt;enumeration value="endTime"/>
 *             &lt;enumeration value="eventCount"/>
 *             &lt;enumeration value="eventId"/>
 *             &lt;enumeration value="eventType"/>
 *             &lt;enumeration value="fileName"/>
 *             &lt;enumeration value="fileType"/>
 *             &lt;enumeration value="httpReferer"/>
 *             &lt;enumeration value="httpRefererDnsDomain"/>
 *             &lt;enumeration value="httpVersion"/>
 *             &lt;enumeration value="message"/>
 *             &lt;enumeration value="name"/>
 *             &lt;enumeration value="newDirName"/>
 *             &lt;enumeration value="newFileName"/>
 *             &lt;enumeration value="oldDirName"/>
 *             &lt;enumeration value="oldFileName"/>
 *             &lt;enumeration value="originator"/>
 *             &lt;enumeration value="rawEvent"/>
 *             &lt;enumeration value="recCollectorId"/>
 *             &lt;enumeration value="requestBody"/>
 *             &lt;enumeration value="requestClientApplication"/>
 *             &lt;enumeration value="requestContext"/>
 *             &lt;enumeration value="requestCookies"/>
 *             &lt;enumeration value="requestHeader"/>
 *             &lt;enumeration value="requestMethod"/>
 *             &lt;enumeration value="requestUrl"/>
 *             &lt;enumeration value="requestUrlQuery"/>
 *             &lt;enumeration value="responseCode"/>
 *             &lt;enumeration value="responseCodeFirst"/>
 *             &lt;enumeration value="severity"/>
 *             &lt;enumeration value="srcAddress"/>
 *             &lt;enumeration value="srcAssetId"/>
 *             &lt;enumeration value="srcDnsDomain"/>
 *             &lt;enumeration value="srcGeoAddress"/>
 *             &lt;enumeration value="srcGeoCity"/>
 *             &lt;enumeration value="srcGeoCountry"/>
 *             &lt;enumeration value="srcGeoCountryCode"/>
 *             &lt;enumeration value="srcGeoCounty"/>
 *             &lt;enumeration value="srcGeoId"/>
 *             &lt;enumeration value="srcGeoIsp"/>
 *             &lt;enumeration value="srcGeoLatitude"/>
 *             &lt;enumeration value="srcGeoLongitude"/>
 *             &lt;enumeration value="srcGeoPostalCode"/>
 *             &lt;enumeration value="srcGeoRegion"/>
 *             &lt;enumeration value="srcGeoRegionCode"/>
 *             &lt;enumeration value="srcHostName"/>
 *             &lt;enumeration value="srcMacAddress"/>
 *             &lt;enumeration value="srcNtDomain"/>
 *             &lt;enumeration value="srcPort"/>
 *             &lt;enumeration value="srcProcessName"/>
 *             &lt;enumeration value="srcServiceName"/>
 *             &lt;enumeration value="srcTransAddress"/>
 *             &lt;enumeration value="srcTransPort"/>
 *             &lt;enumeration value="srcTransZone"/>
 *             &lt;enumeration value="srcUserId"/>
 *             &lt;enumeration value="srcUserName"/>
 *             &lt;enumeration value="srcUserPrivileges"/>
 *             &lt;enumeration value="srcZone"/>
 *             &lt;enumeration value="startTime"/>
 *             &lt;enumeration value="suffix"/>
 *             &lt;enumeration value="transProtocol"/>
 *             &lt;enumeration value="virusName"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
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
