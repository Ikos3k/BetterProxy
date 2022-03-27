package me.Ikos3k.proxy.protocol.data;

import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketDirection;

import java.util.ArrayList;
import java.util.List;

public enum ConnectionState {
    HANDSHAKE, LOGIN, PLAY, STATUS;

    private final List<Packet> clientPackets, serverPackets;

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