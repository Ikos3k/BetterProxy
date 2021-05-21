package me.ANONIMUS.proxy.handler;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;

@Data
public abstract class ServerHandler {
    public final Player player;

    public abstract void disconnected();

    public abstract void handlePacket(final Packet packet);
}