package org.elasticsearch.index.fielddata;

/**
 * Created by xusiao on 2018/5/7.
 */
public interface AtomicGeoPointFieldData extends AtomicFieldData {

    /**
     * Return geo-point values.
     */
    MultiGeoPointValues getGeoPointValues();

}