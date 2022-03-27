package me.Ikos3k.proxy.protocol.packet;

import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.utils.ReflectionUtil;

import java.util.List;

public abstract class Packet {
    public abstract void write(PacketBuffer out, int protocol) throws Exception;

    public abstract void read(PacketBuffer in, int protocol) throws Exception;

    public abstract List<Protocol> getProtocolList();

    @Override
    public String toString() {
        return ReflectionUtil.objectToString(this);
    }
}