package me.ANONIMUS.proxy.protocol.data.playerlist;

import java.util.Arrays;

public enum PlayerListEntryAction {
    ADD_PLAYER(0),
    UPDATE_GAMEMODE(1),
    UPDATE_LATENCY(2),
    UPDATE_DISPLAY_NAME(3),
    REMOVE_PLAYER(4);

    private final int id;

    PlayerListEntryAction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PlayerListEntryAction getById(int id) {
        return Arrays.stream(values()).filter(gt -> gt.id == id).findFirst().get();
    }
}
