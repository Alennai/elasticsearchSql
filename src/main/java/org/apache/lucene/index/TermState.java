package org.apache.lucene.index;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class TermState implements Cloneable {

    /** Sole constructor. (For invocation by subclass
     *  constructors, typically implicit.) */
    protected TermState() {
    }

    /**
     * Copies the content of the given {@link TermState} to this instance
     *
     * @param other
     *          the TermState to copy
     */
    public abstract void copyFrom(TermState other);

    @Override
    public TermState clone() {
        try {
            return (TermState)super.clone();
        } catch (CloneNotSupportedException cnse) {
            // should not happen
            throw new RuntimeException(cnse);
        }
    }

    @Override
    public String toString() {
        return "TermState";
    }
}
