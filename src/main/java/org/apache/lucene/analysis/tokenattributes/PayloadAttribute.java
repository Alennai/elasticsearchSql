package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.BytesRef;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface PayloadAttribute  extends Attribute {
    /**
     * Returns this Token's payload.
     * @see #setPayload(BytesRef)
     */
    public BytesRef getPayload();

    /**
     * Sets this Token's payload.
     * @see #getPayload()
     */
    public void setPayload(BytesRef payload);
}
