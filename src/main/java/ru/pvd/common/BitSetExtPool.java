package ru.pvd.common;

public class BitSetExtPool {

    static final byte MAX_POOL_SIZE = 24;
    static final long MAX_BIT_COUNT = (long) MAX_POOL_SIZE * Integer.MAX_VALUE;

    final int poolBitCount;
    final BitSetExt[] poolBits;

    public BitSetExtPool(long bitCount, byte poolSize) {
        bitCount = Math.min(bitCount, MAX_BIT_COUNT);
        poolSize = (byte) Math.min(poolSize, MAX_POOL_SIZE);

        poolBits = new BitSetExt[poolSize];
        poolBitCount = (int) (bitCount / poolSize + 1);

        for (int i = 0; i < poolBits.length; i++) {
            poolBits[i] = new BitSetExt(poolBitCount);
        }
    }

    public boolean setBitIfNotEquals(long bitIndex, boolean value) {
        int poolIndex = (int) (bitIndex / poolBitCount);
        int poolBitIndex = (int) (bitIndex % poolBitCount);
        return poolBits[poolIndex].setIfNotEquals(poolBitIndex, value);
    }

    public  boolean setBitTrueIfFalse(long bitIndex) {
        return setBitIfNotEquals(bitIndex, true);
    }

    public void clear() {
        for (BitSetExt bit : poolBits) {
            bit.clear();
        }
    }
}
