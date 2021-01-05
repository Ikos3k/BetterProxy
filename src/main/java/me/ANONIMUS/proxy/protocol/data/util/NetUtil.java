package me.ANONIMUS.proxy.protocol.data.util;

import me.ANONIMUS.proxy.protocol.data.*;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetUtil {
    public static EntityMetadata[] readEntityMetadata(final PacketBuffer in) throws IOException {
        final List<EntityMetadata> ret = new ArrayList<>();
        byte b;
        while ((b = in.readByte()) != 127) {
            final int typeId = (b & 0xE0) >> 5;
            final int id = b & 0x1F;
            final MetadataType type = MetadataType.getActionById(typeId);
            Object value;
            switch (type) {
                case BYTE: {
                    value = in.readByte();
                    break;
                }
                case SHORT: {
                    value = in.readShort();
                    break;
                }
                case INT: {
                    value = in.readInt();
                    break;
                }
                case FLOAT: {
                    value = in.readFloat();
                    break;
                }
                case STRING: {
                    value = in.readString();
                    break;
                }
                case ITEM: {
                    value = in.readItemStackFromBuffer();
                    break;
                }
                case BOOLEAN: {
                    value = in.readBoolean();
                    break;
                }
                case POSITION: {
                    value = new Position(in.readInt(), in.readInt(), in.readInt());
                    break;
                }
                case ROTATION: {
                    value = new Rotation(in.readFloat(), in.readFloat(), in.readFloat());
                    break;
                }
                default: {
                    throw new IOException("Unknown metadata type id: " + typeId);
                }
            }
            ret.add(new EntityMetadata(id, type, value));
        }
        return ret.toArray(new EntityMetadata[ret.size()]);
    }

    public static void writeEntityMetadata(final PacketBuffer out, final EntityMetadata[] metadata) throws IOException {
        for (final EntityMetadata meta : metadata) {
            final int id = meta.getType().getId() << 5 | (meta.getId() & 0x1F);
            out.writeByte(id);
            switch (meta.getType()) {
                case BYTE: {
                    out.writeByte((byte) meta.getValue());
                    break;
                }
                case SHORT: {
                    out.writeShort((short) meta.getValue());
                    break;
                }
                case INT: {
                    out.writeInt((int) meta.getValue());
                    break;
                }
                case FLOAT: {
                    out.writeFloat((float) meta.getValue());
                    break;
                }
                case STRING: {
                    out.writeString((String) meta.getValue());
                    break;
                }
                case ITEM: {
                    out.writeItemStackToBuffer((ItemStack) meta.getValue());
                    break;
                }
                case BOOLEAN: {
                    out.writeBoolean((Boolean) meta.getValue());
                    break;
                }
                case POSITION: {
                    final Position pos = (Position)meta.getValue();
                    out.writeInt((int) pos.getX());
                    out.writeInt((int) pos.getY());
                    out.writeInt((int) pos.getZ());
                    break;
                }
                case ROTATION: {
                    final Rotation rot = (Rotation)meta.getValue();
                    out.writeFloat(rot.getX());
                    out.writeFloat(rot.getX());
                    out.writeFloat(rot.getZ());
                    break;
                }
                default: {
                    throw new IOException("Unmapped metadata type: " + meta.getType());
                }
            }
        }
        out.writeByte(127);
    }
}
