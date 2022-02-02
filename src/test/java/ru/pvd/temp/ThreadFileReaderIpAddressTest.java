package ru.pvd.temp;

import org.junit.Assert;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ThreadFileReaderIpAddressTest {

    class ThreadFileReaderIpAddressExt extends ThreadFileReaderIpAddress implements Closeable {

        public ThreadFileReaderIpAddressExt(String file) throws FileNotFoundException {
            super(file, 0, 0, null);
        }

        @Override
        public void close() throws IOException {
            raf.close();
        }

        public long getFilePointer() throws IOException {
            return raf.getFilePointer();
        }

        public long getFileLength() throws IOException {
            return raf.length();
        }
    }

    //    @Test
    public void readLineAsIpAddress() {
        List<int[]> ips = new ArrayList<>(11);
        ips.add(Arrays.stream("94.105.1511.215".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("147.17.1.253".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("209.76.0.112".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("0.0.0.0".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("60.65.228.201".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("0.0.0.0".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("0.0.0.0".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("90.187.156.40".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("0.0.0.0".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("96.159.172.249".split("\\.")).mapToInt(Integer::parseInt).toArray());
        ips.add(Arrays.stream("147.17.1.253".split("\\.")).mapToInt(Integer::parseInt).toArray());

        List<Boolean> ipsIsCorrect = new ArrayList<>(11);
        ipsIsCorrect.add(false); // 94.105.1511.215
        ipsIsCorrect.add(true);  // 147.17.1.253
        ipsIsCorrect.add(true);  // 209.76.0.112
        ipsIsCorrect.add(false); // !101.227.174.148
        ipsIsCorrect.add(true);  // 60.65.228.201
        ipsIsCorrect.add(false); // .
        ipsIsCorrect.add(false); // ...
        ipsIsCorrect.add(true);  // 90.187.156.40
        ipsIsCorrect.add(false); // fewfewfewfew
        ipsIsCorrect.add(true);  // 96.159.172.249
        ipsIsCorrect.add(true);  // 147.17.1.253

        ClassLoader classLoader = ThreadFileReaderIpAddressExt.class.getClassLoader();
        String file = Objects.requireNonNull(classLoader.getResource("ip_addresses.txt")).getFile();
        try (var fileReader = new ThreadFileReaderIpAddressExt(file)) {
            int line = 0;
            boolean result;
            var ip = new int[4];
            long lineCount = ips.size();
            long fileLength = fileReader.getFileLength();
            while ((fileReader.getFilePointer() < fileLength) & (line < lineCount)) {
                result = fileReader.readLineAsIpAddress(ip);
                Assert.assertEquals(ipsIsCorrect.get(line), result);
                if (result) {
                    Assert.assertArrayEquals(ips.get(line), ip);
                }
                line++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}