package me.Ikos3k.proxy.protocol.packet.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;
import me.Ikos3k.proxy.utils.PacketUtil;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomPacket extends Packet {
    private final int customPacketID;
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

    public PacketBuffer getPacketBuffer() {
        PacketBuffer empty = PacketUtil.createEmptyPacketBuffer();
        empty.writeBytes(customData);

        return empty;
    }

    @Override
    public List<Protocol> getProtocolList() {
        return null;
    }
}