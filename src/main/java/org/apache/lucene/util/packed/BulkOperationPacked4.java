package org.apache.lucene.util.packed;

/**
 * Created by xusiao on 2018/5/8.
 */
final class BulkOperationPacked4 extends BulkOperationPacked {

    public BulkOperationPacked4() {
        super(4);
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block = blocks[blocksOffset++];
            for (int shift = 60; shift >= 0; shift -= 4) {
                values[valuesOffset++] = (int) ((block >>> shift) & 15);
            }
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int j = 0; j < iterations; ++j) {
            final byte block = blocks[blocksOffset++];
            values[valuesOffset++] = (block >>> 4) & 15;
            values[valuesOffset++] = block & 15;
        }
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block = blocks[blocksOffset++];
            for (int shift = 60; shift >= 0; shift -= 4) {
                values[valuesOffset++] = (block >>> shift) & 15;
            }
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int j = 0; j < iterations; ++j) {
            final byte block = blocks[blocksOffset++];
            values[valuesOffset++] = (block >>> 4) & 15;
            values[valuesOffset++] = block & 15;
        }
    }

}
