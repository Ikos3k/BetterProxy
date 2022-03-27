package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.chunk.Chunk;
import me.Ikos3k.proxy.protocol.data.chunk.NetworkChunkData;
import me.Ikos3k.proxy.protocol.data.chunk.ParsedChunkData;
import me.Ikos3k.proxy.protocol.data.util.NetUtil;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
public class ServerChunkDataPacket extends Packet {
    private int x;
    private int z;
    private Chunk[] chunks;
    private byte[] biomeData;

    public ServerChunkDataPacket(int x, int z) {
        this(x, z, new Chunk[16], new byte[256]);
    }

    public ServerChunkDataPacket(int x, int z, Chunk[] chunks) {
        this(x, z, chunks, null);
    }

    public ServerChunkDataPacket(int x, int z, Chunk[] chunks, byte[] biomeData) {
        if (chunks.length != 16) {
            throw new IllegalArgumentException("Chunks length must be 16.");
        } else {
            boolean noSkylight = false;
            boolean skylight = false;

            for (Chunk chunk : chunks) {
                if (chunk != null) {
                    if (chunk.getSkylight() == null) {
                        noSkylight = true;
                    } else {
                        skylight = true;
                    }
                }
            }

            if (noSkylight && skylight) {
                throw new IllegalArgumentException("Either all chunks must have skylight values or none must have them.");
            } else {
                this.x = x;
                this.z = z;
                this.chunks = chunks;
                this.biomeData = biomeData;
            }
        }
    }

    public boolean isFullChunk() {
        return this.biomeData != null;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        NetworkChunkData data = NetUtil.chunksToData(new ParsedChunkData(this.chunks, this.biomeData));
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
        boolean fullChunk = in.readBoolean();
        int chunkMask = in.readUnsignedShort();
        byte[] data = in.readByteArray();
        if (data.length > 0) {
            ParsedChunkData chunkData = NetUtil.dataToChunks(new NetworkChunkData(chunkMask, fullChunk, false, data), true);
            this.chunks = chunkData.getChunks();
            this.biomeData = chunkData.getBiomes();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x21, 47));
    }
}