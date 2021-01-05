package me.ANONIMUS.proxy.protocol.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class
ServerLoginSetCompressionPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x03, 47));
        this.getProtocolList().add(new Protocol(0x03, 110));
        this.getProtocolList().add(new Protocol(0x03, 340));
    }

    private int threshold;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.threshold);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.threshold = in.readVarInt();
    }
}
