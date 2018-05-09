package org.apache.lucene.index;

/**
 * Created by xusiao on 2018/5/8.
 */
public class OrdTermState  extends TermState {
    /** Term ordinal, i.e. its position in the full list of
     *  sorted terms. */
    public long ord;

    /** Sole constructor. */
    public OrdTermState() {
    }

    @Override
    public void copyFrom(TermState other) {
        assert other instanceof OrdTermState : "can not copy from " + other.getClass().getName();
        this.ord = ((OrdTermState) other).ord;
    }

    @Override
    public String toString() {
        return "OrdTermState ord=" + ord;
    }
}
