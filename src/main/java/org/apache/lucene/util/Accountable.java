package org.apache.lucene.util;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by xusiao on 2018/5/7.
 */
public interface Accountable {

    /**
     * Return the memory usage of this object in bytes. Negative values are illegal.
     */
    long ramBytesUsed();

    /**
     * Returns nested resources of this class.
     * The result should be a point-in-time snapshot (to avoid race conditions).
     * @see
     */
    default Collection<Accountable> getChildResources() {
        return Collections.emptyList();
    }

}

