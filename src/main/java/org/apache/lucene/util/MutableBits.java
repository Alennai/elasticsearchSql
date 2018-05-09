package org.apache.lucene.util;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface MutableBits  extends Bits {
    /**
     * Sets the bit specified by <code>index</code> to false.
     * @param index index, should be non-negative and &lt; {@link #length()}.
     *        The result of passing negative or out of bounds values is undefined
     *        by this interface, <b>just don't do it!</b>
     */
    public void clear(int index);
}
