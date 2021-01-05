package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.scoreboard.ObjectiveMode;
import me.ANONIMUS.proxy.protocol.data.scoreboard.ObjectiveType;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServerScoreboardObjectivePacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x3B, 47));
        this.getProtocolList().add(new Protocol(0x3F, 110));
        this.getProtocolList().add(new Protocol(0x42, 340));
    }

    private String objectiveName;
    private ObjectiveMode objectiveMode;
    private String objectiveValue;
    private ObjectiveType objectiveType;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.objectiveName);
        out.writeByte(this.objectiveMode.getId());
        if(this.objectiveMode.getId() == 0 || this.objectiveMode.getId() == 2){
            out.writeString(this.objectiveValue);
            out.writeString(this.objectiveType.getValue());
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.objectiveName = in.readString(128);
        this.objectiveMode = ObjectiveMode.getById(in.readByte());
        if(this.objectiveMode.getId() == 0 || this.objectiveMode.getId() == 2){
            this.objectiveValue = in.readString(32767);
            this.objectiveType = ObjectiveType.getByValue(in.readString(32));
        }
    }
}
