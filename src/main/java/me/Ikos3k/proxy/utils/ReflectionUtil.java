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
            return ColorUtil.ANSI_RED + "null";
        }

        StringBuilder text = new StringBuilder(ColorUtil.ANSI_BRIGHT_YELLOW + o.getClass().getSimpleName() + ColorUtil.ANSI_RESET + ColorUtil.ANSI_YELLOW + " ");

        Arrays.stream(o.getClass().getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);

                Object value = field.get(o);

                text.append(ColorUtil.ANSI_YELLOW).append(field.getName()).append(ColorUtil.ANSI_WHITE).append(": ").append(ColorUtil.ANSI_YELLOW);

                if (value == null || value.toString().isEmpty()) {
                    text.append(ColorUtil.ANSI_RED).append("null ").append(ColorUtil.ANSI_YELLOW);
                } else {
                    String type = value.getClass().getSimpleName();

                    if (ArrayUtil.isArray(value)) {
                        text.append(type.replace("[]", "Array")).append("(").append(ColorUtil.ANSI_RESET).append(Array.getLength(value)).append(ColorUtil.ANSI_YELLOW).append("): { ").append(ColorUtil.ANSI_BLUE).append(ArrayUtil.toString(value)).append(ColorUtil.ANSI_YELLOW).append(" }");
                    } else {
                        switch (type) {
                            case "String":
                                text.append("\"" + ColorUtil.ANSI_BRIGHT_GREEN).append(value).append(ColorUtil.ANSI_YELLOW).append("\"");
                                break;
                            case "Boolean":
                            case "Integer":
                                text.append(ColorUtil.ANSI_BRIGHT_GREEN).append(value).append(ColorUtil.ANSI_YELLOW);
                                break;
                            case "Long":
                                text.append(ColorUtil.ANSI_BRIGHT_GREEN).append(value).append(ColorUtil.ANSI_RESET).append("l").append(ColorUtil.ANSI_YELLOW);
                                break;
                            case "Float":
                                text.append(ColorUtil.ANSI_BRIGHT_GREEN).append(value).append(ColorUtil.ANSI_RESET).append("f").append(ColorUtil.ANSI_YELLOW);
                                break;
                            case "Double":
                                text.append(ColorUtil.ANSI_BRIGHT_GREEN).append(value).append(ColorUtil.ANSI_RESET).append("d").append(ColorUtil.ANSI_YELLOW);
                                break;
                            case "Short":
                                text.append(ColorUtil.ANSI_BRIGHT_GREEN).append(value).append(ColorUtil.ANSI_RESET).append("s").append(ColorUtil.ANSI_YELLOW);
                                break;
                            case "Byte":
                                text.append(ColorUtil.ANSI_BRIGHT_GREEN).append(value).append(ColorUtil.ANSI_RESET).append("b").append(ColorUtil.ANSI_YELLOW);
                                break;
                            default:
                                text.append("(").append(type).append("): ").append(ColorUtil.ANSI_PURPLE).append(value).append(ColorUtil.ANSI_YELLOW);
                        }
                    }
                    text.append(" ");
                }
            } catch (IllegalAccessException e) {
                text.append(ColorUtil.ANSI_BACKGROUND_RED).append(o.getClass().getName()).append("@").append(Integer.toHexString(o.hashCode())).append(" (").append(e).append(") ").append(ColorUtil.ANSI_YELLOW);
            }
        });

        return text + ColorUtil.ANSI_RESET;
    }
}