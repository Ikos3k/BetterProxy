package me.ANONIMUS.proxy.protocol.data.playerlist;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PlayerListEntryAction {
    ADD_PLAYER(0),
    UPDATE_GAMEMODE(1),
    UPDATE_LATENCY(2),
    UPDATE_DISPLAY_NAME(3),
    REMOVE_PLAYER(4);

    private final int id;

    public static PlayerListEntryAction getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(null);
    }
}
