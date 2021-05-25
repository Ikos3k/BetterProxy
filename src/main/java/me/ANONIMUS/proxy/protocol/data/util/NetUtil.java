package me.ANONIMUS.proxy.protocol.data.util;

import me.ANONIMUS.proxy.protocol.data.*;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
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
                case INTEGER: {
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
                case INTEGER: {
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
                    final Position pos = (Position) meta.getValue();
                    out.writeInt((int) pos.getX());
                    out.writeInt((int) pos.getY());
                    out.writeInt((int) pos.getZ());
                    break;
                }
                case ROTATION: {
                    final Rotation rot = (Rotation) meta.getValue();
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

    public static ParsedChunkData dataToChunks(NetworkChunkData data, boolean checkForSky) {
        Chunk[] chunks = new Chunk[16];
        int pos = 0;
        int expected = 0;
        boolean sky = false;
        ShortBuffer buf = ByteBuffer.wrap(data.getData()).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        for (int pass = 0; pass < 4; ++pass) {
            for (int ind = 0; ind < 16; ++ind) {
                if ((data.getMask() & 1 << ind) == 0) continue;
                if (pass == 0) {
                    expected += 10240;
                }
                if (pass == 1) {
                    chunks[ind] = new Chunk(sky || data.hasSkyLight());
                    ShortArray3d blocks = chunks[ind].getBlocks();
                    buf.position(pos / 2);
                    buf.get(blocks.getData(), 0, blocks.getData().length);
                    pos += blocks.getData().length * 2;
                }
                if (pass == 2) {
                    NibbleArray3d blocklight = chunks[ind].getBlockLight();
                    System.arraycopy(data.getData(), pos, blocklight.getData(), 0, blocklight.getData().length);
                    pos += blocklight.getData().length;
                }
                if (pass != 3 || !sky && !data.hasSkyLight()) continue;
                NibbleArray3d skylight = chunks[ind].getSkyLight();
                System.arraycopy(data.getData(), pos, skylight.getData(), 0, skylight.getData().length);
                pos += skylight.getData().length;
            }
            if (pass != 0 || data.getData().length <= expected) continue;
            sky = checkForSky;
        }
        byte[] biomeData = null;
        if (data.isFullChunk()) {
            biomeData = new byte[256];
            System.arraycopy(data.getData(), pos, biomeData, 0, biomeData.length);
            pos += biomeData.length;
        }
        return new ParsedChunkData(chunks, biomeData);
    }

    public static NetworkChunkData chunksToData(ParsedChunkData chunks) {
        int chunkMask = 0;
        boolean fullChunk = chunks.getBiomes() != null;
        boolean sky = false;
        int length = fullChunk ? chunks.getBiomes().length : 0;
        byte[] data = null;
        int pos = 0;
        ShortBuffer buf = null;
        for (int pass = 0; pass < 4; ++pass) {
            for (int ind = 0; ind < chunks.getChunks().length; ++ind) {
                Chunk chunk = chunks.getChunks()[ind];
                if (chunk == null || fullChunk && chunk.isEmpty()) continue;
                if (pass == 0) {
                    chunkMask |= 1 << ind;
                    length += chunk.getBlocks().getData().length * 2;
                    length += chunk.getBlockLight().getData().length;
                    if (chunk.getSkyLight() != null) {
                        length += chunk.getSkyLight().getData().length;
                    }
                }
                if (pass == 1) {
                    short[] blocks = chunk.getBlocks().getData();
                    buf.position(pos / 2);
                    buf.put(blocks, 0, blocks.length);
                    pos += blocks.length * 2;
                }
                if (pass == 2) {
                    byte[] blocklight = chunk.getBlockLight().getData();
                    System.arraycopy(blocklight, 0, data, pos, blocklight.length);
                    pos += blocklight.length;
                }
                if (pass != 3 || chunk.getSkyLight() == null) continue;
                byte[] skylight = chunk.getSkyLight().getData();
                System.arraycopy(skylight, 0, data, pos, skylight.length);
                pos += skylight.length;
                sky = true;
            }
            if (pass != 0) continue;
            data = new byte[length];
            buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        }
        if (fullChunk) {
            System.arraycopy(chunks.getBiomes(), 0, data, pos, chunks.getBiomes().length);
            pos += chunks.getBiomes().length;
        }
        return new NetworkChunkData(chunkMask, fullChunk, sky, data);
    }
}