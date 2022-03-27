package me.Ikos3k.proxy.protocol.data.chunk;

import lombok.Data;

@Data
public class ParsedChunkData {
    private final Chunk[] chunks;
    private final byte[] biomes;
}