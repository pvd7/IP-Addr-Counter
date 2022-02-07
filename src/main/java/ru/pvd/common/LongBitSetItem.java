package ru.pvd.common;

public class LongBitSetItem {

    public synchronized boolean setBitTrueIfFalse(long[] words, int wordIndex, long bitIndex) {
        boolean result = (words[wordIndex] & (1L << bitIndex)) == 0;
        if (result) {
            words[wordIndex] |= (1L << bitIndex);
        }
        return result;
    }

}
