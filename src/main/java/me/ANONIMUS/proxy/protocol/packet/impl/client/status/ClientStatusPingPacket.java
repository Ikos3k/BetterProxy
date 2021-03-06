package me.ANONIMUS.proxy.protocol.packet.impl.client.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusPingPacket extends Packet {
    private long time;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeLong(this.time);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.time = in.readLong();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Protocol.protocols(0x01, 47, 109, 110, 210, 340);
    }
}