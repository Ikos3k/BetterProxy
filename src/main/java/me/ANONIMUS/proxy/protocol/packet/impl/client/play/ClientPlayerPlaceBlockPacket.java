package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.Face;
import me.ANONIMUS.proxy.protocol.data.ItemStack;
import me.ANONIMUS.proxy.protocol.data.Position;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientPlayerPlaceBlockPacket extends Packet {
    private Position position;
    private Face face;
    private ItemStack held;
    private float cursorX;
    private float cursorY;
    private float cursorZ;
    private int hand;

    public ClientPlayerPlaceBlockPacket(Position position, Face face, ItemStack held, float cursorX, float cursorY, float cursorZ) {
        this.position = position;
        this.face = face;
        this.held = held;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
    }

    public ClientPlayerPlaceBlockPacket(Position position, Face face, int hand, float cursorX, float cursorY, float cursorZ) {
        this.position = position;
        this.face = face;
        this.hand = hand;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writePosition(position);
        if (protocol >= 110) {
            out.writeVarInt(this.face.getId());
            out.writeVarInt(this.hand);
            if (protocol == 110) {
                out.writeByte((int) this.cursorX);
                out.writeByte((int) this.cursorY);
                out.writeByte((int) this.cursorZ);
            } else {
                out.writeFloat(this.cursorX);
                out.writeFloat(this.cursorY);
                out.writeFloat(this.cursorZ);
            }
        } else {
            out.writeByte(this.face.getId());
            out.writeItemStackToBuffer(held);
            out.writeByte((int) (this.cursorX * 16.0F));
            out.writeByte((int) (this.cursorY * 16.0F));
            out.writeByte((int) (this.cursorZ * 16.0F));
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.position = in.readPosition();
        if (protocol >= 110) {
            this.face = Face.getById(in.readVarInt());
            this.hand = in.readVarInt();
            if (protocol == 110) {
                this.cursorX = in.readByte();
                this.cursorY = in.readByte();
                this.cursorZ = in.readByte();
            } else {
                this.cursorX = in.readFloat();
                this.cursorY = in.readFloat();
                this.cursorZ = in.readFloat();
            }
        } else {
            this.face = Face.getById(in.readUnsignedByte());
            this.held = in.readItemStackFromBuffer();
            this.cursorX = (float) in.readByte() / 16.0F;
            this.cursorY = (float) in.readByte() / 16.0F;
            this.cursorZ = (float) in.readByte() / 16.0F;
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x08, 47), new Protocol(0x1C, 110), new Protocol(0x1F, 340));
    }
}