package me.ANONIMUS.proxy.protocol.data.playerlist;

import me.ANONIMUS.proxy.protocol.data.Gamemode;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;

public class PlayerListEntry {
    private final GameProfile profile;
    private Gamemode gameMode;
    private int ping;
    private String displayName;

    public PlayerListEntry(final GameProfile profile, final Gamemode gameMode, final int ping, final String displayName) {
        this.profile = profile;
        this.gameMode = gameMode;
        this.ping = ping;
        this.displayName = displayName;
    }

    public PlayerListEntry(final GameProfile profile, final Gamemode gameMode) {
        this.profile = profile;
        this.gameMode = gameMode;
    }

    public PlayerListEntry(final GameProfile profile, final int ping) {
        this.profile = profile;
        this.ping = ping;
    }

    public PlayerListEntry(final GameProfile profile, final String displayName) {
        this.profile = profile;
        this.displayName = displayName;
    }

    public PlayerListEntry(final GameProfile profile) {
        this.profile = profile;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public Gamemode getGameMode() {
        return this.gameMode;
    }

    public int getPing() {
        return this.ping;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

