package ru.pvd.common;

import org.junit.Assert;
import org.junit.Test;

public class LongBitPoolTest {

    @Test
    public void setBitTrueIfFalse() {
        // максимальное количество комбинаций IP адресов
        final long MAX_COUNT_IP_ADDRESS = 256L * 256 * 256 * 256;

        BitSetPool pool = new LongBitSetPool(MAX_COUNT_IP_ADDRESS);
        Assert.assertTrue(pool.setBitTrueIfFalse(0));
        Assert.assertFalse(pool.setBitTrueIfFalse(0));
        Assert.assertTrue(pool.setBitTrueIfFalse(MAX_COUNT_IP_ADDRESS - 1));
        Assert.assertFalse(pool.setBitTrueIfFalse(MAX_COUNT_IP_ADDRESS - 1));
    }
}