package me.ANONIMUS.proxy.protocol.data.util;

import me.ANONIMUS.proxy.protocol.data.Chunk;

public class ParsedChunkData {
    private final Chunk[] chunks;
    private final byte[] biomes;

    public ParsedChunkData(Chunk[] chunks, byte[] biomes) {
        this.chunks = chunks;
        this.biomes = biomes;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public byte[] getBiomes() {
        return this.biomes;
    }
}