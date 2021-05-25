package me.ANONIMUS.proxy.protocol.data;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Difficulty {
    PEACEFULL(0), EASY(1), NORMAL(2), HARD(3);

    private final int id;

    Difficulty(int id) {

        this.id = id;
    }

    public static Difficulty getById(int id) {
        return Arrays.stream(values()).filter(gm -> gm.id == id).findFirst().orElse(Difficulty.NORMAL);
    }
}