package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.EntityMetadata;
import me.Ikos3k.proxy.protocol.data.Position;
import me.Ikos3k.proxy.protocol.data.util.NetUtil;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
public class ServerSpawnMobPacket extends Packet {
    private int entityId;
    private byte type;
    private Position position;
    private float pitch;
    private float yaw;
    private float headYaw;
    private double motX;
    private double motY;
    private double motZ;
    private EntityMetadata[] metadata;

    public ServerSpawnMobPacket(int entityId, byte type, Position position, float pitch, float yaw, float headYaw, double motX, double motY, double motZ, EntityMetadata... metadata) {
        this.entityId = entityId;
        this.type = type;
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.headYaw = headYaw;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
        this.metadata = metadata;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.entityId);
        out.writeByte(this.type);
        out.writeInt((int) (this.position.getX() * 32.0));
        out.writeInt((int) (this.position.getY() * 32.0));
        out.writeInt((int) (this.position.getZ() * 32.0));
        out.writeByte((byte) (this.yaw * 256.0f / 360.0f));
        out.writeByte((byte) (this.pitch * 256.0f / 360.0f));
        out.writeByte((byte) (this.headYaw * 256.0f / 360.0f));
        out.writeShort((int) (this.motX * 8000.0));
        out.writeShort((int) (this.motY * 8000.0));
        out.writeShort((int) (this.motZ * 8000.0));
        NetUtil.writeEntityMetadata(out, this.metadata);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.entityId = in.readVarInt();
        this.type = in.readByte();
        this.position = new Position(in.readInt() / 32.0, in.readInt() / 32.0, in.readInt() / 32.0);
        this.yaw = in.readByte() * 360 / 256.0f;
        this.pitch = in.readByte() * 360 / 256.0f;
        this.headYaw = in.readByte() * 360 / 256.0f;
        this.motX = in.readShort() / 8000.0;
        this.motY = in.readShort() / 8000.0;
        this.motZ = in.readShort() / 8000.0;
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0xF, 47));
    }
}
