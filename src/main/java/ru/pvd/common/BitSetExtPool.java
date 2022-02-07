package ru.pvd.common;

public class BitSetExtPool implements BitSetPool {

    // количество блоков BitSet для хранения флага встречающихся IP адресов
    private static final int POOL_SIZE = 128;
    private static final int MAX_POOL_SIZE = 1024;

    static final long MAX_BIT_COUNT = (long) POOL_SIZE * Integer.MAX_VALUE;

    private int poolBitCount;
    private BitSetExt[] poolBits;

    public BitSetExtPool(long bitCount) {
        init(bitCount, POOL_SIZE);
    }

    public BitSetExtPool(long bitCount, int poolSize) {
        init(bitCount, poolSize);
    }

    private void init(long bitCount, int poolSize) {
        bitCount = Math.min(bitCount, MAX_BIT_COUNT);
        poolSize = Math.min(poolSize, MAX_POOL_SIZE);

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

    @Override
    public boolean setBitTrueIfFalse(long bitIndex) {
        return setBitIfNotEquals(bitIndex, true);
    }

    @Override
    public void clear() {
        for (BitSetExt bit : poolBits) {
            bit.clear();
        }
    }
}
