package ru.pvd.common;

import org.junit.Assert;
import org.junit.Test;

public class BitSetExtPoolTest {

    @Test
    public void setBitTrueIfFalse() {
        final long bitCount = 1_000_000;
        final long poolSize = 100;

        for (int j = 1; j < poolSize; j++) {
            BitSetExtPool pool = new BitSetExtPool(bitCount, j);
            int countUnique = 0;

            for (int i = 0; i < 9; i++) {
                if (pool.setBitTrueIfFalse(i * 111)) {
                    countUnique++;
                }
            }
            if (pool.setBitTrueIfFalse(bitCount - 1)) {
                countUnique++;
            }

            for (int i = 0; i < 9; i++) {
                if (pool.setBitTrueIfFalse(i * 111)) {
                    countUnique++;
                }
            }
            if (pool.setBitTrueIfFalse(bitCount - 1)) {
                countUnique++;
            }

            Assert.assertEquals(10, countUnique);
        }
    }
}