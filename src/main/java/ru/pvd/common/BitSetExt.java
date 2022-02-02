package ru.pvd.common;

import java.util.BitSet;

public class BitSetExt extends BitSet {

    public BitSetExt(int nbits) {
        super(nbits);
    }

    public synchronized boolean setIfNotEquals(int bitIndex, boolean value) {
        boolean result = (get(bitIndex) != value);
        if (result) {
            set(bitIndex, value);
        }
        return result;
    }

}
