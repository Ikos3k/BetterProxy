package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ClientKeepAlivePacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x00, 47));
        this.getProtocolList().add(new Protocol(0x0B, 110));
        this.getProtocolList().add(new Protocol(0x0B, 340));
    }

    private long time;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        if(protocol >= 340) {
            out.writeLong(this.time);
        } else {
            out.writeVarInt((int) this.time);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        if(protocol >= 340) {
            this.time = in.readLong();
        } else {
            this.time = in.readVarInt();
        }
    }
}
