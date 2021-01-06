package me.ANONIMUS.proxy.handler;

import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;

public abstract class ServerHandler {
    public final Player player;

    public ServerHandler(Player player) {
        this.player = player;
    }

    public abstract void disconnected();
    public abstract void handlePacket(final Packet packet);
}