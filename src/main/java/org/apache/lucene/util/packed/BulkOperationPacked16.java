package org.apache.lucene.util.packed;

/**
 * Created by xusiao on 2018/5/8.
 */
final class BulkOperationPacked16 extends BulkOperationPacked {

    public BulkOperationPacked16() {
        super(16);
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block = blocks[blocksOffset++];
            for (int shift = 48; shift >= 0; shift -= 16) {
                values[valuesOffset++] = (int) ((block >>> shift) & 65535);
            }
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int j = 0; j < iterations; ++j) {
            values[valuesOffset++] = ((blocks[blocksOffset++] & 0xFF) << 8) | (blocks[blocksOffset++] & 0xFF);
        }
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block = blocks[blocksOffset++];
            for (int shift = 48; shift >= 0; shift -= 16) {
                values[valuesOffset++] = (block >>> shift) & 65535;
            }
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int j = 0; j < iterations; ++j) {
            values[valuesOffset++] = ((blocks[blocksOffset++] & 0xFFL) << 8) | (blocks[blocksOffset++] & 0xFFL);
        }
    }

}
