package me.Ikos3k.proxy.protocol.data.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ObjectiveMode {
    CREATE(0), REMOVE(1), UPDATE(2);

    private final int id;

    public static ObjectiveMode getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(CREATE);
    }
}