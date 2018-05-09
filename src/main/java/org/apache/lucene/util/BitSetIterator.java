package org.apache.lucene.util;

import org.apache.lucene.search.DocIdSetIterator;

/**
 * Created by xusiao on 2018/5/8.
 */
public class BitSetIterator  extends DocIdSetIterator {

    private static <T extends BitSet> T getBitSet(DocIdSetIterator iterator, Class<? extends T> clazz) {
        if (iterator instanceof BitSetIterator) {
            BitSet bits = ((BitSetIterator) iterator).bits;
            assert bits != null;
            if (clazz.isInstance(bits)) {
                return clazz.cast(bits);
            }
        }
        return null;
    }

    /** If the provided iterator wraps a {@link FixedBitSet}, returns it, otherwise returns null. */
    public static FixedBitSet getFixedBitSetOrNull(DocIdSetIterator iterator) {
        return getBitSet(iterator, FixedBitSet.class);
    }

    /** If the provided iterator wraps a {@link SparseFixedBitSet}, returns it, otherwise returns null. */
    public static SparseFixedBitSet getSparseFixedBitSetOrNull(DocIdSetIterator iterator) {
        return getBitSet(iterator, SparseFixedBitSet.class);
    }

    private final BitSet bits;
    private final int length;
    private final long cost;
    private int doc = -1;

    /** Sole constructor. */
    public BitSetIterator(BitSet bits, long cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be >= 0, got " + cost);
        }
        this.bits = bits;
        this.length = bits.length();
        this.cost = cost;
    }

    /** Return the wrapped {@link BitSet}. */
    public BitSet getBitSet() {
        return bits;
    }

    @Override
    public int docID() {
        return doc;
    }

    /** Set the current doc id that this iterator is on. */
    public void setDocId(int docId) {
        this.doc = docId;
    }

    @Override
    public int nextDoc() {
        return advance(doc + 1);
    }

    @Override
    public int advance(int target) {
        if (target >= length) {
            return doc = NO_MORE_DOCS;
        }
        return doc = bits.nextSetBit(target);
    }

    @Override
    public long cost() {
        return cost;
    }

}

