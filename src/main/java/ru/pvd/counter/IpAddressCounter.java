package ru.pvd.counter;

import ru.pvd.common.BitSetExtPool;

import java.io.*;

public class IpAddressCounter implements UniqueInetAddressCheck {

    // максимальное количество комбинаций IP адресов
    private static final long MAX_COUNT_IP_ADDRESS = 256 * 256 * 256 * 256L;
    // количество блоков BitSet для хранения флага встречающихся IP адресов
    private static final byte BITSET_POOL_SIZE = 24;

    private final BitSetExtPool bitSetPool;
    private long countUniqueIpAddress = 0;

    private long spentTimeMillis = 0;

    public IpAddressCounter() {
        bitSetPool = new BitSetExtPool(MAX_COUNT_IP_ADDRESS, BITSET_POOL_SIZE);
    }

    public long calc(String file, Integer threadCount) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        if (countUniqueIpAddress > 0) {
            bitSetPool.clear();
        }

        long fileLength = new File(file).length();
        long partLength = fileLength / threadCount;

        ThreadFileReaderIpAddress[] fileReaders = new ThreadFileReaderIpAddress[threadCount];

        for (int i = 0; i < fileReaders.length; i++) {
            fileReaders[i] = new ThreadFileReaderIpAddress(file, i * partLength, partLength, this);
            fileReaders[i].start();
        }

        for (Thread thread : fileReaders) {
            thread.join();
        }

        for (var fileReader : fileReaders) {
            countUniqueIpAddress += fileReader.getCountUniqueIpAddress();
        }

        spentTimeMillis = System.currentTimeMillis() - startTime;

        return countUniqueIpAddress;
    }

    @Override
    public boolean uniqueIpAddress(int[] ip) {
        long bit = ip[0] + (ip[1] + (ip[2] + ip[3] * 256L) * 256) * 256;
        return bitSetPool.setBitTrueIfFalse(bit);
    }

    public long getCountUniqueIpAddress() {
        return countUniqueIpAddress;
    }

    public long getSpentTimeMillis() {
        return spentTimeMillis;
    }
}

