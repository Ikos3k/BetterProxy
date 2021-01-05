package me.ANONIMUS.proxy.protocol.packet.impl.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HandshakePacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x00, 0));
    }

    private int protocolId;
    private String host;
    private int port;
    private int nextState;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.protocolId);
        out.writeString(this.host);
        out.writeShort(this.port);
        out.writeVarInt(this.nextState);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.protocolId = in.readVarInt();
        this.host = in.readString(128);
        this.port = in.readShort();
        this.nextState = in.readVarInt();
    }
}