package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface TypeAttribute  extends Attribute {

    /** the default type */
    public static final String DEFAULT_TYPE = "word";

    /**
     * Returns this Token's lexical type.  Defaults to "word".
     * @see #setType(String)
     */
    public String type();

    /**
     * Set the lexical type.
     * @see #type()
     */
    public void setType(String type);
}

