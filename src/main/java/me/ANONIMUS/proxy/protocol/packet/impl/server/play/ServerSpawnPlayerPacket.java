package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.EntityMetadata;
import me.ANONIMUS.proxy.protocol.data.util.NetUtil;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import net.minecraft.util.MathHelper;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ServerSpawnPlayerPacket extends Packet {
    private int entityID;
    private UUID uuid;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int currentItem;
    private EntityMetadata[] metadata;

    public ServerSpawnPlayerPacket(int entityID, UUID uuid, double x, double y, double z, float yaw, float pitch, int currentItem, EntityMetadata[] metadata)
    {
        this.entityID = entityID;
        this.uuid = uuid;
        this.x = MathHelper.floor_double(x * 32.0D);
        this.y = MathHelper.floor_double(y * 32.0D);
        this.z = MathHelper.floor_double(z * 32.0D);
        this.yaw = yaw;
        this.pitch = pitch;
        this.currentItem = currentItem;
        this.metadata = metadata;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.entityID);
        out.writeUuid(this.uuid);
        out.writeInt((int) (this.x));
        out.writeInt((int) (this.y));
        out.writeInt((int) (this.z));
        out.writeByte((byte) (this.yaw));
        out.writeByte((byte) (this.pitch));
        out.writeShort(this.currentItem);
        NetUtil.writeEntityMetadata(out, this.metadata);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.entityID = in.readVarInt();
        this.uuid = in.readUuid();
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
        this.yaw = in.readByte();
        this.pitch = in.readByte();
        this.currentItem = in.readShort();
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x0C, 47));
    }
}