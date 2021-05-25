package me.ANONIMUS.proxy.protocol.data.scoreboard;

import java.util.Arrays;

public enum PositionType {
    LIST(0), SIDEBAR(1), BELOWNAME(2);

    private final int id;

    PositionType(int id) {
        this.id = id;
    }

    public static PositionType getById(int id) {
        return Arrays.stream(values()).filter(pos -> pos.id == id).findFirst().orElse(PositionType.LIST);
    }

    public int getId() {
        return this.id;
    }
}
