package me.ANONIMUS.proxy.protocol.data;

import java.util.Arrays;

public enum Face {
    BOTTOM(0),
    TOP(1),
    EAST(2),
    WEST(3),
    NORTH(4),
    SOUTH(5),
    SPECIAL(255);

    private int id;

    Face(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static Face getById(int id) {
        return Arrays.stream(values()).filter((gm) -> gm.id == id).findFirst().orElse(BOTTOM);
    }
}
