package org.elasticsearch.common.lease;

import java.io.Closeable;

/**
 * Created by xusiao on 2018/5/7.
 */
public interface Releasable  extends Closeable {

    @Override
    void close();

}
