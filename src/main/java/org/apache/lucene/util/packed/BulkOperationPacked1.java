package org.apache.lucene.util.packed;

/**
 * Created by xusiao on 2018/5/8.
 */
final class BulkOperationPacked1 extends BulkOperationPacked {

    public BulkOperationPacked1() {
        super(1);
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block = blocks[blocksOffset++];
            for (int shift = 63; shift >= 0; shift -= 1) {
                values[valuesOffset++] = (int) ((block >>> shift) & 1);
            }
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int j = 0; j < iterations; ++j) {
            final byte block = blocks[blocksOffset++];
            values[valuesOffset++] = (block >>> 7) & 1;
            values[valuesOffset++] = (block >>> 6) & 1;
            values[valuesOffset++] = (block >>> 5) & 1;
            values[valuesOffset++] = (block >>> 4) & 1;
            values[valuesOffset++] = (block >>> 3) & 1;
            values[valuesOffset++] = (block >>> 2) & 1;
            values[valuesOffset++] = (block >>> 1) & 1;
            values[valuesOffset++] = block & 1;
        }
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block = blocks[blocksOffset++];
            for (int shift = 63; shift >= 0; shift -= 1) {
                values[valuesOffset++] = (block >>> shift) & 1;
            }
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int j = 0; j < iterations; ++j) {
            final byte block = blocks[blocksOffset++];
            values[valuesOffset++] = (block >>> 7) & 1;
            values[valuesOffset++] = (block >>> 6) & 1;
            values[valuesOffset++] = (block >>> 5) & 1;
            values[valuesOffset++] = (block >>> 4) & 1;
            values[valuesOffset++] = (block >>> 3) & 1;
            values[valuesOffset++] = (block >>> 2) & 1;
            values[valuesOffset++] = (block >>> 1) & 1;
            values[valuesOffset++] = block & 1;
        }
    }

}
