package ru.pvd.util;

public class StringUtil {

    public enum Direction {
        FORWARD,
        BACK
    }

    public static String formatNumberUnderline(int number) {
        return insertTextEveryFewCharacters(String.valueOf(number), "_", 3, Direction.BACK);
    }

    public static String insertTextEveryFewCharacters(String source, String insert, int interval, Direction direction) {
        StringBuilder sb = new StringBuilder(String.valueOf(source));
        int len = sb.length();
        int offset = (direction == Direction.FORWARD ? len % interval : interval);
        if (offset == 0) {
            offset = interval;
        }
        for (int i = len - offset; i > 0; i -= interval) {
            sb.insert(i, insert);
        }
        return sb.toString();
    }

}
