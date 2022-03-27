package me.Ikos3k.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MetadataType {
    BYTE(0),
    SHORT(1),
    INTEGER(2),
    FLOAT(3),
    STRING(4),
    ITEM(5),
    POSITION(6),
    ROTATION(7),
    BOOLEAN(8);

    private final int id;

    public static MetadataType getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(null);
    }
}

