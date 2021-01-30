package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.Difficulty;
import me.ANONIMUS.proxy.protocol.data.Dimension;
import me.ANONIMUS.proxy.protocol.data.Gamemode;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerJoinGamePacket extends Packet {
    private int entityId;
    private Gamemode gamemode;
    private Dimension dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private String levelType;
    private boolean reduced_debug;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeInt(this.entityId);
        out.writeByte(this.gamemode.getId());
        if (protocol >= 109) {
            out.writeInt(this.dimension.getId());
        } else {
            out.writeByte(this.dimension.getId());
        }
        out.writeByte(this.difficulty.getId());
        out.writeByte(this.maxPlayers);
        out.writeString(this.levelType);
        out.writeBoolean(this.reduced_debug);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.entityId = in.readInt();
        this.gamemode = Gamemode.getById(in.readUnsignedByte());
        if (protocol >= 109) {
            this.dimension = Dimension.getById(in.readInt());
        } else {
            this.dimension = Dimension.getById(in.readByte());
        }
        this.difficulty = Difficulty.getById(in.readUnsignedByte());
        this.maxPlayers = in.readUnsignedByte();
        this.levelType = in.readString(16);

        if (this.levelType == null) {
            this.levelType = "default";
        }

        this.reduced_debug = in.readBoolean();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x01, 47), new Protocol(0x23, 109), new Protocol(0x23, 110), new Protocol(0x23, 210), new Protocol(0x23, 340));
    }
}