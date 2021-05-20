package me.ANONIMUS.proxy.protocol.data.scoreboard;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ObjectiveMode {

    CREATE(0), REMOVE(1), UPDATE(2);

    private final int id;

    ObjectiveMode(int id) {
        this.id = id;
    }

    public static ObjectiveMode getById(int id) {
        return Arrays.stream(ObjectiveMode.values()).filter(ob -> ob.id == id).findFirst().orElse(ObjectiveMode.CREATE);
    }
}
