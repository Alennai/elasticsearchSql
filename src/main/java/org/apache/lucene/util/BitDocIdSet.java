package org.apache.lucene.util;

import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;

/**
 * Created by xusiao on 2018/5/8.
 */
public class BitDocIdSet  extends DocIdSet {

    private static final long BASE_RAM_BYTES_USED = RamUsageEstimator.shallowSizeOfInstance(BitDocIdSet.class);

    private final BitSet set;
    private final long cost;

    /**
     * Wrap the given {@link BitSet} as a {@link DocIdSet}. The provided
     * {@link BitSet} must not be modified afterwards.
     */
    public BitDocIdSet(BitSet set, long cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be >= 0, got " + cost);
        }
        this.set = set;
        this.cost = cost;
    }

    /**
     * Same as {@link #BitDocIdSet(BitSet, long)} but uses the set's
     * {@link BitSet#approximateCardinality() approximate cardinality} as a cost.
     */
    public BitDocIdSet(BitSet set) {
        this(set, set.approximateCardinality());
    }

    @Override
    public DocIdSetIterator iterator() {
        return new BitSetIterator(set, cost);
    }

    @Override
    public BitSet bits() {
        return set;
    }

    @Override
    public long ramBytesUsed() {
        return BASE_RAM_BYTES_USED + set.ramBytesUsed();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(set=" + set + ",cost=" + cost + ")";
    }

}

