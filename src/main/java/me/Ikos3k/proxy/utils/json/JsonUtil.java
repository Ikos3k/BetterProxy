package me.Ikos3k.proxy.utils.json;

import lombok.SneakyThrows;
import me.Ikos3k.proxy.utils.NumberUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.*;

public class JsonUtil {
    public static String toJsonString(Object object, boolean format) throws IOException {
        JSONObject jsonObject = new JSONObject();

        getFieldsWithAnnotion(object).forEach((fields, annotionString) -> {
            boolean annotion = annotionString != null && !annotionString.isEmpty();

            JSONObject annotionObject = new JSONObject();
            for (Field field : fields) {
                field.setAccessible(true);

                Object fieldName = field.getName();
                Object fieldValue = null;
                try {
                    fieldValue = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (annotion) {
                    annotionObject.put(fieldName, fieldValue);
                } else {
                    jsonObject.put(fieldName, fieldValue);
                }
            }
            if (annotion) {
                jsonObject.put(annotionString, annotionObject);
            }
        });

        if (format) {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        }
        return jsonObject.toJSONString();
    }

    @SneakyThrows
    public static <T> T fromJson(Reader reader, T c) {
        StringBuilder json = new StringBuilder();
        int charValue;
        while ((charValue = reader.read()) != -1) {
            json.append((char) charValue);
        }
        reader.close();

        Object obj = new JSONParser().parse(json.toString());

        getFieldsWithAnnotion(c).forEach((fields, annotion) -> {
            JSONObject jsonObj = (JSONObject) obj;

            if (annotion != null && !annotion.isEmpty()) {
                jsonObj = (JSONObject) jsonObj.get(annotion);
            }

            for (Field f : c.getClass().getDeclaredFields()) {
                for (Field field : fields) {
                    if (f.getName().equals(field.getName())) {
                        f.setAccessible(true);
                        try {
                            Object value = jsonObj.get(f.getName());
                            if (value instanceof Number) {
                                f.set(c, NumberUtil.getNumber(f.get(c), value));
                                break;
                            }

                            if (value instanceof String && f.getType().isEnum()) {
                                f.set(c, Enum.valueOf((Class<Enum>) f.getType(), (String) value));
                                break;
                            }

                            f.set(c, value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        return c;
    }

    private static Map<List<Field>, String> getFieldsWithAnnotion(Object object) {
        Map<List<Field>, String> fields = new HashMap<>();

        int i = 0;
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonInfo.class)) {
                int x = i + 1;
                List<Field> fieldList = new ArrayList<>(Collections.singletonList(field));
                Field f;
                while (object.getClass().getDeclaredFields().length != x
                        && !(f = object.getClass().getDeclaredFields()[x]).isAnnotationPresent(JsonInfo.class)) {
                    fieldList.add(f);
                    x++;
                }
                if (!field.getAnnotation(JsonInfo.class).object().isEmpty()) {
                    fields.put(fieldList, field.getAnnotation(JsonInfo.class).object());
                } else {
                    fields.put(fieldList, null);
                }
            }
            i++;
        }
        return fields;
    }
}