package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface OffsetAttribute extends Attribute {
    /**
     * Returns this Token's starting offset, the position of the first character
     * corresponding to this token in the source text.
     * <p>
     * Note that the difference between {@link #endOffset()} and <code>startOffset()</code>
     * may not be equal to termText.length(), as the term text may have been altered by a
     * stemmer or some other filter.
     * @see #setOffset(int, int)
     */
    public int startOffset();


    /**
     * Set the starting and ending offset.
     * @throws IllegalArgumentException If <code>startOffset</code> or <code>endOffset</code>
     *         are negative, or if <code>startOffset</code> is greater than
     *         <code>endOffset</code>
     * @see #startOffset()
     * @see #endOffset()
     */
    public void setOffset(int startOffset, int endOffset);


    /**
     * Returns this Token's ending offset, one greater than the position of the
     * last character corresponding to this token in the source text. The length
     * of the token in the source text is (<code>endOffset()</code> - {@link #startOffset()}).
     * @see #setOffset(int, int)
     */
    public int endOffset();
}

