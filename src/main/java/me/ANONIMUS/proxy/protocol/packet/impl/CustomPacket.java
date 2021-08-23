package me.ANONIMUS.proxy.protocol.packet.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomPacket extends Packet {
    private int customPacketID;
    private byte[] customData;

    public CustomPacket(int id) {
        this.customPacketID = id;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeBytes(customData);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.customData = new byte[in.readableBytes()];
        in.readBytes(customData);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return null;
    }
}