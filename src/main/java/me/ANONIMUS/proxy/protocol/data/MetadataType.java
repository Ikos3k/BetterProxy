package me.ANONIMUS.proxy.protocol.data;

import java.util.Arrays;

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

    MetadataType(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MetadataType getActionById(final int id) {
        return Arrays.stream(values()).filter(a -> a.getId() == id).findFirst().orElse(null);
    }
}

