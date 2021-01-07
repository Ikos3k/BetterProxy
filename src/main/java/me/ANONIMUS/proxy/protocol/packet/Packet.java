package me.ANONIMUS.proxy.protocol.packet;

import lombok.Data;

import java.util.List;

@Data
public abstract class Packet {
    public abstract void write(PacketBuffer out, int protocol) throws Exception;

    public abstract void read(PacketBuffer in, int protocol) throws Exception;

    public abstract List<Protocol> getProtocolList();
}