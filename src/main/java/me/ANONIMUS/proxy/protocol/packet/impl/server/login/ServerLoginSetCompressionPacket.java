package me.ANONIMUS.proxy.protocol.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerLoginSetCompressionPacket extends Packet {
    private int threshold;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.threshold);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.threshold = in.readVarInt();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x03, 47, 107, 108, 109, 110, 210, 315, 335, 338, 340));
    }
}