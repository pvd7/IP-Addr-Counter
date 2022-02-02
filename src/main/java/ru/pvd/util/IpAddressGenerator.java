package ru.pvd.util;

import java.io.*;

import static ru.pvd.util.StringUtil.formatNumberUnderline;

public class IpAddressGenerator {

    public static void generate(String file, int count) {
        file = file + formatNumberUnderline(count);
        StringBuilder sb = new StringBuilder();
        try (var out = new FileWriter(file)) {
            for (int i = 0; i < count; i++) {
                sb.setLength(0);
                sb.append((int) (Math.random() * 256))
                        .append('.')
                        .append((int) (Math.random() * 256))
                        .append('.')
                        .append((int) (Math.random() * 256))
                        .append('.')
                        .append((int) (Math.random() * 256))
                        .append('\n');
                out.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        IpAddressGenerator.generate("c:\\db\\ip_addresses_", 1_000_000_000);
        System.out.println(System.currentTimeMillis() - startTime);
    }
}