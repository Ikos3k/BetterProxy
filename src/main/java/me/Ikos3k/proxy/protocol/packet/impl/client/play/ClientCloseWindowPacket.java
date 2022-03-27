package me.Ikos3k.proxy.protocol.packet.impl.client.play;

import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ClientCloseWindowPacket extends Packet {
    private int windowId;

    @Override
    public void write(PacketBuffer out, int protocol) throws IOException {
        out.writeByte(this.windowId);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws IOException {
        this.windowId = in.readByte();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x0D, 47));
    }
}