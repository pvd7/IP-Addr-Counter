package ru.pvd;

import ru.pvd.counter.IpAddressCounter;

import java.io.*;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        String file = "c:\\db\\ip_addresses";
//        String file = "c:\\db\\ip_addresses_100_000_000";
//        String file = "c:\\db\\ip_addresses_1_000_000";
//        String file = "c:\\db\\ip_addresses_1_000_000_000";
//        String file = "c:\\db\\ip_addresses_10";

        int threadCount = Runtime.getRuntime().availableProcessors();

        System.out.printf("File size: %,d byte\n", new File(file).length());
        var counter = new IpAddressCounter();
        System.out.printf("Number of unique IPs: %,d\n", counter.calc(file, threadCount));
        System.out.printf("Time of processing (%d - threads): %,d ms\n", threadCount, counter.getSpentTimeMillis());
    }
}
