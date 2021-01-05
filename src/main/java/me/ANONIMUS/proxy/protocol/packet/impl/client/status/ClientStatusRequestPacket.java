package me.ANONIMUS.proxy.protocol.packet.impl.client.status;

import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

public class ClientStatusRequestPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x00, 47));
        this.getProtocolList().add(new Protocol(0x00, 110));
        this.getProtocolList().add(new Protocol(0x00, 340));
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception { }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception { }
}
