package me.Ikos3k.proxy.protocol.data.status;

import lombok.Data;
import me.Ikos3k.proxy.protocol.objects.GameProfile;

@Data
public class PlayerInfo {
    private final int onlinePlayers, maxPlayers;
    private final GameProfile[] players;
}