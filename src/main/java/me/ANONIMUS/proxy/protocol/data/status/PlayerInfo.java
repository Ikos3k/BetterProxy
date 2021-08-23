package me.ANONIMUS.proxy.protocol.data.status;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;

@Data
public class PlayerInfo {
    private final int onlinePlayers, maxPlayers;
    private final GameProfile[] players;
}