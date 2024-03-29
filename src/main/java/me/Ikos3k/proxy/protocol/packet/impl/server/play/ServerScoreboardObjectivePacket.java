package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.scoreboard.ObjectiveMode;
import me.Ikos3k.proxy.protocol.data.scoreboard.ObjectiveType;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerScoreboardObjectivePacket extends Packet {
    private String objectiveName;
    private ObjectiveMode objectiveMode;
    private String objectiveValue;
    private ObjectiveType objectiveType;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.objectiveName);
        out.writeByte(this.objectiveMode.getId());
        if (this.objectiveMode.getId() == 0 || this.objectiveMode.getId() == 2) {
            out.writeString(this.objectiveValue);
            out.writeString(this.objectiveType.getValue());
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.objectiveName = in.readString(128);
        this.objectiveMode = ObjectiveMode.getById(in.readByte());
        if (this.objectiveMode.getId() == 0 || this.objectiveMode.getId() == 2) {
            this.objectiveValue = in.readString(32767);
            this.objectiveType = ObjectiveType.getById(in.readString(32));
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x3B, 47), new Protocol(0x3F, 107, 108, 109, 110, 210, 315, 316), new Protocol(0x41, 335), new Protocol(0x42, 338, 340));
    }
}