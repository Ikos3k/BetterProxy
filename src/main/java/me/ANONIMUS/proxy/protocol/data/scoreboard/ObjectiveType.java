package me.ANONIMUS.proxy.protocol.data.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ObjectiveType {
    INTEGER("integer"),
    HEARTS("hearts");

    private final String value;

    public static ObjectiveType getById(String value) {
        return Arrays.stream(values()).filter(v -> v.value.equals(value)).findFirst().orElse(INTEGER);
    }
}
