package me.Ikos3k.proxy.utils;

import org.reflections.Reflections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtil {
    public static <T> List<? extends T> getClasses(String prefix, Class<T> classType) {
        return new Reflections(prefix).getSubTypesOf(classType).stream().map(c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }).collect(Collectors.toList());
    }

    public static String objectToString(Object o) {
        if(o == null) {
            return "null";
        }

        return o.getClass().getSimpleName() + " " + Arrays.stream(o.getClass().getDeclaredFields()).map(field -> {
            try {
                field.setAccessible(true);

                Object value = field.get(o);

                if (value == null || value.toString().isEmpty()) {
                    return field.getName() + ": null";
                }

                String name = value.getClass().getSimpleName();
                if (ArrayUtil.isArray(value)) {
                    return field.getName() + ": " + name.replace("[]", "Array") + "(" + Array.getLength(value) + "): { " + ArrayUtil.toString(value) + " }";
                }

                return field.getName() + ": (" + name + "): " + value;
            } catch (IllegalAccessException e) {
                return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode()) + " (" + e + ")";
            }
        }).collect(Collectors.toList());
    }
}