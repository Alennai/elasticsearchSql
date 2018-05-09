package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface PositionIncrementAttribute extends Attribute {
    /** Set the position increment. The default value is one.
     *
     * @param positionIncrement the distance from the prior term
     * @throws IllegalArgumentException if <code>positionIncrement</code>
     *         is negative.
     * @see #getPositionIncrement()
     */
    public void setPositionIncrement(int positionIncrement);

    /** Returns the position increment of this Token.
     * @see #setPositionIncrement(int)
     */
    public int getPositionIncrement();
}

