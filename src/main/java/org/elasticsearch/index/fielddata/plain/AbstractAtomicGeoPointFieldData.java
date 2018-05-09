package org.elasticsearch.index.fielddata.plain;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by xusiao on 2018/5/7.
 */
public class AbstractAtomicGeoPointFieldData implements AtomicGeoPointFieldData {

    @Override
    public final SortedBinaryDocValues getBytesValues() {
        return FieldData.toString(getGeoPointValues());
    }

    @Override
    public final ScriptDocValues.GeoPoints getScriptValues() {
        return new ScriptDocValues.GeoPoints(getGeoPointValues());
    }

    public static AtomicGeoPointFieldData empty(final int maxDoc) {
        return new AbstractAtomicGeoPointFieldData() {

            @Override
            public long ramBytesUsed() {
                return 0;
            }

            @Override
            public Collection<Accountable> getChildResources() {
                return Collections.emptyList();
            }

            @Override
            public void close() {
            }

            @Override
            public MultiGeoPointValues getGeoPointValues() {
                return FieldData.emptyMultiGeoPoints(maxDoc);
            }
        };
    }
}