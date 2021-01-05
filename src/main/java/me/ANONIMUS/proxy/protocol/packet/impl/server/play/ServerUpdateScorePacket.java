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
public class ServerUpdateScorePacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x3C, 47));
        this.getProtocolList().add(new Protocol(0x42, 110));
        this.getProtocolList().add(new Protocol(0x45, 340));
    }

    private String scoreName;
    private int action;
    private String objectiveName;
    private int value;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.scoreName);
        out.writeByte(this.action);
        out.writeString(this.objectiveName);
        if(action != 1){
            out.writeVarInt(this.value);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.scoreName = in.readString(128);
        this.action = in.readByte();
        this.objectiveName = in.readString(32767);
        if(action != 1){
            this.value = in.readVarInt();
        }
    }
}
