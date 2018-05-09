package org.apache.lucene.util;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface Bits {
    /**
     * Returns the value of the bit with the specified <code>index</code>.
     * @param index index, should be non-negative and &lt; {@link #length()}.
     *        The result of passing negative or out of bounds values is undefined
     *        by this interface, <b>just don't do it!</b>
     * @return <code>true</code> if the bit is set, <code>false</code> otherwise.
     */
    public boolean get(int index);

    /** Returns the number of bits in this set */
    public int length();

    public static final Bits[] EMPTY_ARRAY = new Bits[0];

    /**
     * Bits impl of the specified length with all bits set.
     */
    public static class MatchAllBits implements Bits {
        final int len;

        public MatchAllBits( int len ) {
            this.len = len;
        }

        @Override
        public boolean get(int index) {
            return true;
        }

        @Override
        public int length() {
            return len;
        }
    }

    /**
     * Bits impl of the specified length with no bits set.
     */
    public static class MatchNoBits implements Bits {
        final int len;

        public MatchNoBits( int len ) {
            this.len = len;
        }

        @Override
        public boolean get(int index) {
            return false;
        }

        @Override
        public int length() {
            return len;
        }
    }
}
