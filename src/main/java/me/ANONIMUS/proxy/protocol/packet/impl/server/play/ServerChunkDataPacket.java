package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.Chunk;
import me.ANONIMUS.proxy.protocol.data.util.NetUtil;
import me.ANONIMUS.proxy.protocol.data.util.NetworkChunkData;
import me.ANONIMUS.proxy.protocol.data.util.ParsedChunkData;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
public class ServerChunkDataPacket extends Packet {
    public ServerChunkDataPacket(final int x, final int z) {
        this(x, z, new Chunk[16], new byte[256]);
    }

    public ServerChunkDataPacket(final int x, final int z, final Chunk[] chunks) {
        this(x, z, chunks, null);
    }

    public ServerChunkDataPacket(final int x, final int z, final Chunk[] chunks, final byte[] biomeData) {
        if (chunks.length != 16) {
            throw new IllegalArgumentException("Chunks length must be 16.");
        }
        boolean noSkylight = false;
        boolean skylight = false;
        for (Chunk chunk : chunks) {
            if (chunk != null) {
                if (chunk.getSkyLight() == null) {
                    noSkylight = true;
                } else {
                    skylight = true;
                }
            }
        }
        if (noSkylight && skylight) {
            throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
        }
        this.x = x;
        this.z = z;
        this.chunks = chunks;
        this.biomeData = biomeData;
    }

    private int x;
    private int z;
    private Chunk[] chunks;
    private byte[] biomeData;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        final NetworkChunkData data = NetUtil.chunksToData(new ParsedChunkData(this.chunks, this.biomeData));
        out.writeInt(this.x);
        out.writeInt(this.z);
        out.writeBoolean(data.isFullChunk());
        out.writeShort(data.getMask());
        out.writeVarInt(data.getData().length);
        out.writeBytes(data.getData(), data.getData().length);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.x = in.readInt();
        this.z = in.readInt();
        final boolean fullChunk = in.readBoolean();
        final int chunkMask = in.readUnsignedShort();
        final byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        final ParsedChunkData chunkData = NetUtil.dataToChunks(new NetworkChunkData(chunkMask, fullChunk, false, data), true);
        this.chunks = chunkData.getChunks();
        this.biomeData = chunkData.getBiomes();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x21, 47));
    }

    public boolean isFullChunk() {
        return this.biomeData != null;
    }
}