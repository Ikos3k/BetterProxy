package me.ANONIMUS.proxy.protocol.packet;

public enum PacketDirection {
    SERVERBOUND("CLIENT"), CLIENTBOUND("SERVER");

    final String packetsPackageName;

    PacketDirection(String s) {
        this.packetsPackageName = s;
    }
}