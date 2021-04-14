package me.ANONIMUS.proxy.protocol.data;

import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;

import java.util.ArrayList;
import java.util.List;

public enum ConnectionState {
    HANDSHAKE, LOGIN, PLAY, STATUS;

    private final List<Packet> clientPackets;
    private final List<Packet> serverPackets;

    ConnectionState() {
        this.clientPackets = new ArrayList<>();
        this.serverPackets = new ArrayList<>();
    }

    public List<Packet> getPacketsByDirection(PacketDirection direction) {
        switch (direction) {
            case SERVERBOUND:
                return clientPackets;
            case CLIENTBOUND:
                return serverPackets;
        }
        return null;
    }
}