package ru.pvd.common;

import org.junit.Assert;
import org.junit.Test;

public class BitSetExtTest {

    @Test
    public void setIfNotEquals() {
        int nbits = 100;
        BitSetExt bits = new BitSetExt(nbits);
        for (int i = 0; i < nbits; i++) {
            bits.set(i);
            Assert.assertFalse(bits.setIfNotEquals(i, true));
        }

        BitSetExt bits2 = new BitSetExt(nbits);
        for (int i = 0; i < nbits; i++) {
            Assert.assertTrue(bits2.setIfNotEquals(i, true));
            Assert.assertFalse(bits2.setIfNotEquals(i, true));
        }
    }
}