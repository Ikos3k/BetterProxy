package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerPlayerPosLookPacket extends Packet {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int flags;
    private int teleport;

    public ServerPlayerPosLookPacket(double x, double y, double z, float yaw, float pitch, int flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    public ServerPlayerPosLookPacket(double x, double y, double z, float yaw, float pitch) {
        this(x, y, z, yaw, pitch, 0);
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        out.writeByte(this.flags);
        if (protocol >= 107) {
            out.writeVarInt(this.teleport);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.flags = in.readUnsignedByte();
        if (protocol >= 107) {
            this.teleport = in.readVarInt();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x08, 47), new Protocol(0x2E, 107, 108, 109, 110, 210, 315, 316, 335), new Protocol(0x2F, 338, 340));
    }
}