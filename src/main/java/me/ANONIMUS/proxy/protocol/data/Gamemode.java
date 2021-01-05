package me.ANONIMUS.proxy.protocol.data;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gamemode {

    SURVIVAL(0), CREATIVE(1), ADVENTURE(2), SPECTATOR(3), HARDCORE(0x8);

    private final int id;

    Gamemode(int id) {
        this.id = id;
    }

    public static Gamemode getById(int id) {
        return Arrays.stream(Gamemode.values()).filter(gm -> gm.id == id).findFirst().orElse(Gamemode.SURVIVAL);
    }
}
