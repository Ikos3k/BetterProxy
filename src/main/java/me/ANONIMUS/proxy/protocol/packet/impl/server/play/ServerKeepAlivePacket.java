package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerKeepAlivePacket extends Packet {
    private long keepaliveId;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        if (protocol >= 340) {
            out.writeLong(this.keepaliveId);
        } else {
            out.writeVarInt((int) this.keepaliveId);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        if (protocol >= 340) {
            this.keepaliveId = in.readLong();
        } else {
            this.keepaliveId = in.readVarInt();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x00, 47), new Protocol(0x1F, 109), new Protocol(0x1F, 110), new Protocol(0x1F, 210), new Protocol(0x1F, 340));
    }
}