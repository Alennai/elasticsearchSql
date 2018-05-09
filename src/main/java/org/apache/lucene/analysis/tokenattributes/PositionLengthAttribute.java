package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface PositionLengthAttribute  extends Attribute {
    /**
     * Set the position length of this Token.
     * <p>
     * The default value is one.
     * @param positionLength how many positions this token
     *  spans.
     * @throws IllegalArgumentException if <code>positionLength</code>
     *         is zero or negative.
     * @see #getPositionLength()
     */
    public void setPositionLength(int positionLength);

    /** Returns the position length of this Token.
     * @see #setPositionLength
     */
    public int getPositionLength();
}