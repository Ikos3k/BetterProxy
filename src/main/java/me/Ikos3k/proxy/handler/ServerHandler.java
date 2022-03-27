package me.Ikos3k.proxy.handler;

import lombok.Data;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet;

@Data
public abstract class ServerHandler {
    public final Player player;

    public abstract void disconnect();

    public abstract void handlePacket(final Packet packet);
}