package me.Ikos3k.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gamemode {
    SURVIVAL(0), CREATIVE(1), ADVENTURE(2), SPECTATOR(3), HARDCORE(0x8);

    private final int id;

    public static Gamemode getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(SURVIVAL);
    }
}