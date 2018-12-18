/*

  @author shaco.zhu
 * Date:2017年5月25日上午9:14:24

 */
package org.parc.restes.entity;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class IField {
    private String alias;
    private String name;
    private String type;
    private Map<String, Object> meta;
    private boolean agg;

    /**
     *
     */
    public IField() {
        super();
    }

    public IField(String name, JSONObject mt) {
        this.name = name;
//		this.alias = DictionaryCache.field(name);
        this.meta = mt;
    }

    public IField(String name, String alias, JSONObject mt) {
        this.name = name;
        if (StringUtils.isBlank(alias)) {
//			this.alias = DictionaryCache.field(name);
        } else {
            this.alias = alias;
        }
        this.meta = mt;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the meta
     */
    public Map<String, Object> getMeta() {
        return meta;
    }

    /**
     * @param meta the meta to set
     */
    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name + ":" + alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAgg() {
        return agg;
    }

    public void setAgg(boolean agg) {
        this.agg = agg;
    }

}
