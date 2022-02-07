package ru.pvd.common;

import java.util.Arrays;

public class LongBitSetPool implements BitSetPool {

    /*
     * BitSets are packed into arrays of "words." Currently, a word is
     * a long, which consists of 64 bits, requiring 6 address bits.
     * The choice of word size is determined purely by performance concerns.
     */
    private static final int ADDRESS_BITS_PER_WORD = 6;

    public static final int POOL_SIZE = 512;

    private final long[] words;
    private final LongBitSetItem[] pool;
    private final int itemPoolSize;

    public LongBitSetPool(long bitCount) {
        int size = wordIndex(bitCount - 1) + 1;
        words = new long[size];

        itemPoolSize = size / POOL_SIZE;
        pool = new LongBitSetItem[POOL_SIZE];
        Arrays.setAll(pool, i -> new LongBitSetItem());
    }

    @Override
    public boolean setBitTrueIfFalse(long bitIndex) {
        int wordIndex = wordIndex(bitIndex);
        int indexInPool = wordIndex / itemPoolSize;
        return pool[indexInPool].setBitTrueIfFalse(words, wordIndex, bitIndex);
    }

    @Override
    public void clear() {
        Arrays.fill(words, 0);
    }

    private static int wordIndex(long bitIndex) {
        return (int) (bitIndex >> ADDRESS_BITS_PER_WORD);
    }

}
