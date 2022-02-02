package ru.pvd.counter;

import org.junit.Assert;
import org.junit.Test;
import ru.pvd.temp.ThreadFileReaderIpAddressTest;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IpAddressCounterTest {

    final ClassLoader classLoader = ThreadFileReaderIpAddressTest.class.getClassLoader();
//    final String file = Objects.requireNonNull(classLoader.getResource("ip_addresses_1_000_err")).getFile();
    final String file = Objects.requireNonNull(classLoader.getResource("ip_addresses_10_000")).getFile();

    public static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }

            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private long getCountUniqueIpAddressByHashMap() {
        Map<String, String> mapIpAddress = new HashMap<>();
        String line;
        try (var raf = new RandomAccessFile(file, "r")) {
            while ((line = raf.readLine()) != null) {
                if (validIP(line)) {
                    mapIpAddress.putIfAbsent(line, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapIpAddress.size();
    }

    private long getCountUniqueIpAddressByIpAddressCounter() throws InterruptedException {
        int threadCount = Runtime.getRuntime().availableProcessors();
        var ips = new IpAddressCounter();
        ips.calc(file, threadCount);
        return ips.getCountUniqueIpAddress();
    }

    @Test
    public void testCalcCountUniqueIpAddress() throws InterruptedException {
        long countUniqueIpAddressByIpAddressCounter = getCountUniqueIpAddressByIpAddressCounter();
        long countUniqueIpAddressByHashMap = getCountUniqueIpAddressByHashMap();
        Assert.assertEquals(countUniqueIpAddressByHashMap, countUniqueIpAddressByIpAddressCounter);
    }

}