package me.ANONIMUS.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Face {
    BOTTOM(0), TOP(1), EAST(2), WEST(3), NORTH(4), SOUTH(5), SPECIAL(255);

    private final int id;

    public static Face getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(BOTTOM);
    }
}