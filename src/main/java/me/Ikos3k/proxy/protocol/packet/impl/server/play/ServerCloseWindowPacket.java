package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerCloseWindowPacket extends Packet {
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
        return Collections.singletonList(new Protocol(0x2E, 47));
    }
}