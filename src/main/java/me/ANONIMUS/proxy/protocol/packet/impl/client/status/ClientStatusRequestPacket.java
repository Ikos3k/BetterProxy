package me.ANONIMUS.proxy.protocol.packet.impl.client.status;

import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.List;

public class ClientStatusRequestPacket extends Packet {
    @Override
    public void write(PacketBuffer out, int protocol) throws Exception { }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception { }

    @Override
    public List<Protocol> getProtocolList() {
        return Protocol.protocols(0x00, 47, 109, 110, 210, 340);
    }
}