package me.ANONIMUS.proxy.handler;

import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.objects.Player;

public abstract class ServerHandler {
    public final Player player;

    public ServerHandler(Player player) {
        this.player = player;
    }

    public abstract void disconnected();
    public abstract void handlePacket(final Packet packet);
}