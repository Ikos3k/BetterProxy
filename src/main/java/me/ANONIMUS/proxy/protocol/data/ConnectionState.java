package me.ANONIMUS.proxy.protocol.data;

import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ConnectionState {
    HANDSHAKE, LOGIN, PLAY, STATUS;

    private final Map<Packet, List<Protocol>> clientPackets;
    private final Map<Packet, List<Protocol>> serverPackets;

    ConnectionState() {
        this.clientPackets = new HashMap<>();
        this.serverPackets = new HashMap<>();
    }

    public Map<Packet, List<Protocol>> getPacketsByDirection(PacketDirection direction) {
        switch (direction) {
            case SERVERBOUND:
                return clientPackets;
            case CLIENTBOUND:
                return serverPackets;
        }
        return null;
    }
}