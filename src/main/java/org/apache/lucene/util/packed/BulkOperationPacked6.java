package org.apache.lucene.util.packed;

/**
 * Created by xusiao on 2018/5/8.
 */
final class BulkOperationPacked6 extends BulkOperationPacked {

    public BulkOperationPacked6() {
        super(6);
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block0 = blocks[blocksOffset++];
            values[valuesOffset++] = (int) (block0 >>> 58);
            values[valuesOffset++] = (int) ((block0 >>> 52) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 46) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 40) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 34) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 28) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 22) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 16) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 10) & 63L);
            values[valuesOffset++] = (int) ((block0 >>> 4) & 63L);
            final long block1 = blocks[blocksOffset++];
            values[valuesOffset++] = (int) (((block0 & 15L) << 2) | (block1 >>> 62));
            values[valuesOffset++] = (int) ((block1 >>> 56) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 50) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 44) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 38) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 32) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 26) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 20) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 14) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 8) & 63L);
            values[valuesOffset++] = (int) ((block1 >>> 2) & 63L);
            final long block2 = blocks[blocksOffset++];
            values[valuesOffset++] = (int) (((block1 & 3L) << 4) | (block2 >>> 60));
            values[valuesOffset++] = (int) ((block2 >>> 54) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 48) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 42) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 36) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 30) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 24) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 18) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 12) & 63L);
            values[valuesOffset++] = (int) ((block2 >>> 6) & 63L);
            values[valuesOffset++] = (int) (block2 & 63L);
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, int[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final int byte0 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = byte0 >>> 2;
            final int byte1 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = ((byte0 & 3) << 4) | (byte1 >>> 4);
            final int byte2 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = ((byte1 & 15) << 2) | (byte2 >>> 6);
            values[valuesOffset++] = byte2 & 63;
        }
    }

    @Override
    public void decode(long[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long block0 = blocks[blocksOffset++];
            values[valuesOffset++] = block0 >>> 58;
            values[valuesOffset++] = (block0 >>> 52) & 63L;
            values[valuesOffset++] = (block0 >>> 46) & 63L;
            values[valuesOffset++] = (block0 >>> 40) & 63L;
            values[valuesOffset++] = (block0 >>> 34) & 63L;
            values[valuesOffset++] = (block0 >>> 28) & 63L;
            values[valuesOffset++] = (block0 >>> 22) & 63L;
            values[valuesOffset++] = (block0 >>> 16) & 63L;
            values[valuesOffset++] = (block0 >>> 10) & 63L;
            values[valuesOffset++] = (block0 >>> 4) & 63L;
            final long block1 = blocks[blocksOffset++];
            values[valuesOffset++] = ((block0 & 15L) << 2) | (block1 >>> 62);
            values[valuesOffset++] = (block1 >>> 56) & 63L;
            values[valuesOffset++] = (block1 >>> 50) & 63L;
            values[valuesOffset++] = (block1 >>> 44) & 63L;
            values[valuesOffset++] = (block1 >>> 38) & 63L;
            values[valuesOffset++] = (block1 >>> 32) & 63L;
            values[valuesOffset++] = (block1 >>> 26) & 63L;
            values[valuesOffset++] = (block1 >>> 20) & 63L;
            values[valuesOffset++] = (block1 >>> 14) & 63L;
            values[valuesOffset++] = (block1 >>> 8) & 63L;
            values[valuesOffset++] = (block1 >>> 2) & 63L;
            final long block2 = blocks[blocksOffset++];
            values[valuesOffset++] = ((block1 & 3L) << 4) | (block2 >>> 60);
            values[valuesOffset++] = (block2 >>> 54) & 63L;
            values[valuesOffset++] = (block2 >>> 48) & 63L;
            values[valuesOffset++] = (block2 >>> 42) & 63L;
            values[valuesOffset++] = (block2 >>> 36) & 63L;
            values[valuesOffset++] = (block2 >>> 30) & 63L;
            values[valuesOffset++] = (block2 >>> 24) & 63L;
            values[valuesOffset++] = (block2 >>> 18) & 63L;
            values[valuesOffset++] = (block2 >>> 12) & 63L;
            values[valuesOffset++] = (block2 >>> 6) & 63L;
            values[valuesOffset++] = block2 & 63L;
        }
    }

    @Override
    public void decode(byte[] blocks, int blocksOffset, long[] values, int valuesOffset, int iterations) {
        for (int i = 0; i < iterations; ++i) {
            final long byte0 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = byte0 >>> 2;
            final long byte1 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = ((byte0 & 3) << 4) | (byte1 >>> 4);
            final long byte2 = blocks[blocksOffset++] & 0xFF;
            values[valuesOffset++] = ((byte1 & 15) << 2) | (byte2 >>> 6);
            values[valuesOffset++] = byte2 & 63;
        }
    }

}

