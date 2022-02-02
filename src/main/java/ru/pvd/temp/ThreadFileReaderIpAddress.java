package ru.pvd.temp;

import ru.pvd.counter.UniqueInetAddressCheck;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ThreadFileReaderIpAddress extends Thread {

    //количество символов в максимальном по длине IP адресе 255.255.255.255
    final int MAX_LENGTH_IP_ADDRESS = 15;

    final RandomAccessFile raf;
    final long startPos;
    final long len;
    final UniqueInetAddressCheck addressCheck;

    long countUniqueIpAddress = 0;

    public ThreadFileReaderIpAddress(String file, long startPos, long len, UniqueInetAddressCheck addressCheck) throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, "r");
        this.startPos = startPos;
        this.len = len;
        this.addressCheck = addressCheck;
    }

    @Override
    public void run() {
        var ip = new int[4];
        try {
            raf.seek(startPos);
            // Если начальная позиция > 0, то getFilePointer() скорее всего указывает не начало строки,
            // поэтому делаем "холостое" чтение, что бы переместить указатель на начало следующе строки
            if (startPos > 0) {
                raf.readLine();
            }

            // Прибавляем MAX_LENGTH_IP_ADDRESS, т.к. будет сдвиг указателя в файле в следующей части,
            // переданной на обработку в следующий Thread
            long endPos = startPos + len + MAX_LENGTH_IP_ADDRESS;
            if (endPos > raf.length()) {
                endPos = raf.length();
            }

            while (raf.getFilePointer() < endPos) {
                if (readLineAsIpAddress(ip)) {
                    if (addressCheck.uniqueIpAddress(ip)) {
                        countUniqueIpAddress++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean readLineAsIpAddress(int[] ip) throws IOException {
        int c;
        boolean endOfIp = false;
        // индекс части ip адреса, который сейчас читаем
        byte i = 0;
        // если true, то ожидаем, что прочитаем из файла число 0..9
        boolean newNumExpected = true;

        int num;
        // если true, то ip адрес не верный, а значит надо пропустить эту строку
        boolean skipLine = false;

        while (!endOfIp) {
            switch (c = raf.read()) {
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                    if (skipLine) {
                        break;
                    }
                    num = Character.getNumericValue(c);
                    if (newNumExpected) {
                        ip[i] = num;
                        newNumExpected = false;
                    } else {
                        ip[i] = ip[i] * 10 + num;
                        if (ip[i] > 255) {
                            skipLine = true;
                        }
                    }
                }
                case '.' -> {
                    if (skipLine) {
                        break;
                    }
                    if (newNumExpected || (i == 3)) {
                        skipLine = true;
                    } else {
                        newNumExpected = true;
                        i++;
                    }
                }
                case -1, '\n' -> endOfIp = true;
                case '\r' -> {
                    endOfIp = true;
                    long cur = raf.getFilePointer();
                    if ((raf.read()) != '\n') {
                        raf.seek(cur);
                    }
                }
                default -> skipLine = true;
            }
        }
        // true - ip address, false - not ip address
        return (!skipLine & !newNumExpected & (i == 3));
    }

    public long getCountUniqueIpAddress() {
        return countUniqueIpAddress;
    }

}
