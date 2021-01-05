package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServerDisplayScoreboardPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x3D, 47));
        this.getProtocolList().add(new Protocol(0x38, 110));
        this.getProtocolList().add(new Protocol(0x3B, 340));
    }

    private int position;
    private String scoreName;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeByte(this.position);
        out.writeString(this.scoreName);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.position = in.readByte();
        this.scoreName = in.readString(32767);
    }
}
