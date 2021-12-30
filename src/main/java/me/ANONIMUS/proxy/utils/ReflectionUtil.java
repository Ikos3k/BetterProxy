package me.ANONIMUS.proxy.utils;

import org.reflections.Reflections;

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
}