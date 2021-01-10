package me.ANONIMUS.proxy.protocol.data;

import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionState {
    HANDSHAKE, LOGIN, PLAY, STATUS;

    private final Map<Protocol, Packet> clientPackets;
    private final Map<Protocol, Packet> serverPackets;

    ConnectionState() {
        this.clientPackets = new HashMap<>();
        this.serverPackets = new HashMap<>();
    }

    public Map<Protocol, Packet> getPacketsByDirection(PacketDirection direction) {
        switch (direction) {
            case SERVERBOUND:
                return clientPackets;
            case CLIENTBOUND:
                return serverPackets;
        }
        return null;
    }
}