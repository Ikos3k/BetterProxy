package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.Position;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServerPlayerPosLookPacket extends Packet {
    private Position position;
    private float yaw;
    private float pitch;
    private boolean onGround;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeDouble(this.position.getX());
        out.writeDouble(this.position.getY());
        out.writeDouble(this.position.getZ());
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        out.writeByte((byte) (this.onGround ? 1 : 0));
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        final double x = in.readDouble();
        final double y = in.readDouble();
        final double z = in.readDouble();
        this.position = new Position(x, y, z);
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.onGround = in.readByte() == 1;
    }
}
