package me.ANONIMUS.proxy.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtil {
    public static int[] compress(byte[] byteArray) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        List<Integer> numbersList = new ArrayList<>();
        int currentNumber = byteArray[0];
        int numbers = 1;

        for (int i = 1; i < byteArray.length; i++) {
            if (byteArray[i] == currentNumber) {
                numbers++;
            } else {
                numbersList.add(numbers);
                numbersList.add(currentNumber);
                dos.writeInt(numbers);
                dos.writeInt(currentNumber);

                currentNumber = byteArray[i];
                numbers = 1;
            }
        }

        dos.writeInt(numbers);
        dos.writeInt(currentNumber);

        numbersList.add(numbers);
        numbersList.add(currentNumber);

        int[] test = new int[numbersList.size()];
        for(int i = 0; i < numbersList.size(); i++) {
            test[i] = numbersList.get(i);
        }

        return test;
    }

    public static byte[] decompress(byte[] bytesArray) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        for (int i = 0; i < bytesArray.length; i += 2) {
            for(int d = 0; d < bytesArray[i]; d++) {
                dos.writeInt(bytesArray[i + 1]);
            }
        }

        return baos.toByteArray();
    }

    public static List<Integer> toList(int[] bytes) {
        List<Integer> list = new ArrayList<>();
        for(int b : bytes) { list.add(b); }

        return list;
    }

    public static int getCompressSizeDifference(byte[] byteArray) {
        try {
            return byteArray.length - compress(byteArray).length;
        } catch (IOException e) {
            return 0;
        }
    }

    public static Object getValue(Object array) {
        switch (array.getClass().getSimpleName()) {
            case "byte[]": { return Arrays.toString((byte[]) array); }
            case "long[]": { return Arrays.toString((long[]) array); }
            case "float[]": { return Arrays.toString((float[]) array); }
            case "short[]": { return Arrays.toString((short[]) array); }
            case "double[]": { return Arrays.toString((double[]) array) ; }
            case "int[]": { return Arrays.toString((int[]) array); }
            default: return Arrays.toString((Object[]) array);
        }
    }

    public static boolean isArray(Object object) {
        return object.getClass().getSimpleName().endsWith("[]");
    }
}