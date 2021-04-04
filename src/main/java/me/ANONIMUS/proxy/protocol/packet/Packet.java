package me.ANONIMUS.proxy.protocol.packet;

import me.ANONIMUS.proxy.protocol.Protocol;

import java.util.List;

public abstract class Packet {
    public abstract List<Protocol> getProtocolList();

    public abstract void write(PacketBuffer out, int protocol) throws Exception;

    public abstract void read(PacketBuffer in, int protocol) throws Exception;
}