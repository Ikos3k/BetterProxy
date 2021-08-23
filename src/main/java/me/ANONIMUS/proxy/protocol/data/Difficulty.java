package me.ANONIMUS.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Difficulty {
    PEACEFULL(0), EASY(1), NORMAL(2), HARD(3);

    private final int id;

    public static Difficulty getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(NORMAL);
    }
}