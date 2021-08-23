package me.ANONIMUS.proxy.utils;

import org.reflections.Reflections;

import java.util.Set;

public class ReflectionUtil {
    public static <T> Set<Class<? extends T>> getClasses(String prefix, Class<T> classType) {
        return new Reflections(prefix).getSubTypesOf(classType);
    }
}