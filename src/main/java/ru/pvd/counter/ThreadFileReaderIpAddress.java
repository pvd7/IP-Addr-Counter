package ru.pvd.counter;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ThreadFileReaderIpAddress extends Thread {

    //количество символов в IP адресе 255.255.255.255
    private static final int MAX_LENGTH_IP_ADDRESS = 15;
    private static final int BUFFER_SIZE = 512 * 1024;

    private final String file;
    private final long startPos;
    private final long endPos;
    private final UniqueInetAddressCheck addressCheck;

    private long countUniqueIpAddress = 0;

    public ThreadFileReaderIpAddress(String file, long startPos, long endPos, UniqueInetAddressCheck addressCheck) {
        this.file = file;
        this.startPos = startPos;

        // Прибавляем MAX_LENGTH_IP_ADDRESS, т.к. мы сдвигаем startPos на начало следующей строки
        this.endPos = endPos + MAX_LENGTH_IP_ADDRESS;

        this.addressCheck = addressCheck;
    }

    @Override
    public void run() {
        var ip = new int[4];
        try (var raf = new RandomAccessFile(file, "r")) {
            if (startPos > 0) {
                raf.seek(startPos);
                // делаем "холостое" чтение, что бы переместить startPos на начало следующе строки
                raf.readLine();
            }

            // буфер
            byte[] buf = new byte[BUFFER_SIZE];
            // сколько байт из файла записано в буфер
            int bytesRead = -1;
            // индекс части ip адреса, который сейчас заполняем
            byte indexIp = 0;
            // если true, то ожидаем, что следующее значение в буфере будет число 0..9
            boolean expectedNumber = true;
            //
            int num;
            // если true, то ip адрес не верный, а значит пропускаем эту строку
            boolean invalidIp = false;
            //
            long currPos;

            while (((currPos = raf.getFilePointer()) <= endPos) && ((bytesRead = raf.read(buf)) != -1)) {
                // проверяем не достигли ли конечной позиции в обрабатываемой части фала,
                // если да, то уменьшаем количество прочитанных байт в буфер, что бы не обрабатывать лишнее
                if (bytesRead > (endPos - currPos)) {
                    bytesRead = (int) (endPos - currPos);
                }
                for (int i = 0; i < bytesRead; i++) {
                    switch (buf[i]) {
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                            if (!invalidIp) {
                                num = Character.getNumericValue(buf[i]);
                                if (expectedNumber) {
                                    ip[indexIp] = num;
                                    expectedNumber = false;
                                } else {
                                    ip[indexIp] = ip[indexIp] * 10 + num;
                                    if (ip[indexIp] > 255) {
                                        invalidIp = true;
                                    }
                                }
                            }
                        }
                        case '.' -> {
                            if (!invalidIp) {
                                if (expectedNumber || (indexIp == 3)) {
                                    invalidIp = true;
                                } else {
                                    expectedNumber = true;
                                    indexIp++;
                                }
                            }
                        }
                        case '\n', '\r' -> {
                            if (!invalidIp & !expectedNumber & (indexIp == 3)) {
                                if (addressCheck.uniqueIpAddress(ip)) {
                                    countUniqueIpAddress++;
                                }
                            }
                            invalidIp = false;
                            indexIp = 0;
                            expectedNumber = true;
                        }
                        default -> invalidIp = true;
                    }
                }
            }
            if ((bytesRead == -1) & !invalidIp & !expectedNumber & (indexIp == 3)) {
                if (addressCheck.uniqueIpAddress(ip)) {
                    countUniqueIpAddress++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getCountUniqueIpAddress() {
        return countUniqueIpAddress;
    }

}
