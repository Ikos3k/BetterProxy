package me.ANONIMUS.proxy.utils;

public class NumberUtil {
    public static Object getNumber(Object numType, Object number) {
        switch (numType.getClass().getSimpleName()) {
            case "Long": {
                return ((Number) number).longValue();
            }
            case "Short": {
                return ((Number) number).shortValue();
            }
            case "Float": {
                return ((Number) number).floatValue();
            }
            case "Double": {
                return ((Number) number).doubleValue();
            }
            case "Byte": {
                return ((Number) number).byteValue();
            }
            case "Integer": {
                return ((Number) number).intValue();
            }
            default:
                return number;
        }
    }
}