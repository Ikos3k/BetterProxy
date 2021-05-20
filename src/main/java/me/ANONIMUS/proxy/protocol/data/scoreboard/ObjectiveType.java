package me.ANONIMUS.proxy.protocol.data.scoreboard;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ObjectiveType {
    INTEGER("integer"),
    HEARTS("hearts");

    private final String value;

    ObjectiveType(String value) {
        this.value = value;
    }

    public static ObjectiveType getByValue(String val) {
        return Arrays.stream(ObjectiveType.values()).filter(ob -> ob.value.equals(val)).findFirst().orElse(ObjectiveType.INTEGER);
    }
}
