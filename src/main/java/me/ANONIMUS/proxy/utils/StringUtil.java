package me.ANONIMUS.proxy.utils;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtil {
    public static int getStringWidth(final String s) {
        int width = 0;
        for (char c : s.toCharArray()) {
            width += getCharWidth(c);
        }
        return width;
    }

    public static int getCharWidth(final char c) {
        switch (c) {
            case '`': {
                return 0;
            }
            case '|':
            case 'i': {
                return 2;
            }
            case 'l': {
                return 3;
            }
            case 't':
            case 'I':
            case ' ': {
                return 4;
            }
            case 'k':
            case '\"':
            case '*':
            case 'f': {
                return 5;
            }
            case 'm':
            case 'n':
            case 'o':
            case 'j':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'g':
            case 'h':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '+':
            case '&':
            case '#':
            case '_':
            case '$':
            case '%': {
                return 6;
            }
            case '~':
            case '@': {
                return 7;
            }
        }
        return 1;
    }

    public static String generateString(int length) {
        final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        return IntStream.range(0, length).mapToObj(i -> Character.toString(chars[new Random().nextInt(chars.length)])).collect(Collectors.joining());
    }
}