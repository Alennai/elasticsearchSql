package org.apache.lucene.util;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface AttributeReflector {

    /**
     * This method gets called for every property in an {@link AttributeImpl}/{@link AttributeSource}
     * passing the class name of the {@link Attribute}, a key and the actual value.
     * E.g., an invocation of
     * would call this method once using {@code org.apache.lucene.analysis.tokenattributes.CharTermAttribute.class}
     * as attribute class, {@code "term"} as key and the actual value as a String.
     */
    public void reflect(Class<? extends Attribute> attClass, String key, Object value);

}