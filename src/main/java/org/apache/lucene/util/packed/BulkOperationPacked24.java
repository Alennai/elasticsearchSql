package org.apache.lucene.util.packed;

/**
 * Created by xusiao on 2018/5/8.
 */
final class BulkOperationPacked24 extends BulkOperationPacked {

    public BulkOperationPacked24() {
        super(24);
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block0 = blocks[blocksOffset++];
            values[valuesOffset++] = (int) (block0 >>> 40);
            values[valuesOffset++] = (int) ((block0 >>> 16) & 16777215L);
            final long block1 = blocks[blocksOffset++];
            values[valuesOffset++] = (int) (((block0 & 65535L) << 8) | (block1 >>> 56));
            values[valuesOffset++] = (int) ((block1 >>> 32) & 16777215L);
            values[valuesOffset++] = (int) ((block1 >>> 8) & 16777215L);
            final long block2 = blocks[blocksOffset++];
            values[valuesOffset++] = (int) (((block1 & 255L) << 16) | (block2 >>> 48));
            values[valuesOffset++] = (int) ((block2 >>> 24) & 16777215L);
            values[valuesOffset++] = (int) (block2 & 16777215L);
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final int byte0 = blocks[blocksOffset++] & 0xFF;
            final int byte1 = blocks[blocksOffset++] & 0xFF;
            final int byte2 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = (byte0 << 16) | (byte1 << 8) | byte2;
        }
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block0 = blocks[blocksOffset++];
            values[valuesOffset++] = block0 >>> 40;
            values[valuesOffset++] = (block0 >>> 16) & 16777215L;
            final long block1 = blocks[blocksOffset++];
            values[valuesOffset++] = ((block0 & 65535L) << 8) | (block1 >>> 56);
            values[valuesOffset++] = (block1 >>> 32) & 16777215L;
            values[valuesOffset++] = (block1 >>> 8) & 16777215L;
            final long block2 = blocks[blocksOffset++];
            values[valuesOffset++] = ((block1 & 255L) << 16) | (block2 >>> 48);
            values[valuesOffset++] = (block2 >>> 24) & 16777215L;
            values[valuesOffset++] = block2 & 16777215L;
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long byte0 = blocks[blocksOffset++] & 0xFF;
            final long byte1 = blocks[blocksOffset++] & 0xFF;
            final long byte2 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = (byte0 << 16) | (byte1 << 8) | byte2;
        }
    }

}
