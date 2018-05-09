package org.elasticsearch.index.fielddata;

import org.apache.lucene.util.Accountable;
import org.elasticsearch.common.lease.Releasable;

/**
 * Created by xusiao on 2018/5/7.
 */
public interface AtomicFieldData  extends Accountable, Releasable {

    /**
     * Returns a "scripting" based values.
     */
    ScriptDocValues getScriptValues();

    /**
     * Return a String representation of the values.
     */
    SortedBinaryDocValues getBytesValues();

}