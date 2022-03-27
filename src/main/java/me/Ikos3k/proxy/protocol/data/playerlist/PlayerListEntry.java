package me.Ikos3k.proxy.protocol.data.playerlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.Ikos3k.proxy.protocol.data.Gamemode;
import me.Ikos3k.proxy.protocol.objects.GameProfile;

@Data
@AllArgsConstructor
public class PlayerListEntry {
    private final GameProfile profile;
    private Gamemode gameMode;
    private int ping;
    private String displayName;

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
}

