package me.ANONIMUS.proxy.protocol.data.util;

public class NetworkChunkData {
    private final int mask;
    private final boolean fullChunk;
    private final boolean sky;
    private final byte[] data;

    public NetworkChunkData(int mask, boolean fullChunk, boolean sky, byte[] data) {
        this.mask = mask;
        this.fullChunk = fullChunk;
        this.sky = sky;
        this.data = data;
    }

    public int getMask() {
        return this.mask;
    }

    public boolean isFullChunk() {
        return this.fullChunk;
    }

    public boolean hasSkyLight() {
        return this.sky;
    }

    public byte[] getData() {
        return this.data;
    }
}