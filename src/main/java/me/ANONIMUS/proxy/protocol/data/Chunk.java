package me.ANONIMUS.proxy.protocol.data;

public class Chunk {
    private final ShortArray3d blocks;
    private final NibbleArray3d blocklight;
    private final NibbleArray3d skylight;

    public Chunk(final boolean skylight) {
        this(new ShortArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
    }

    public Chunk(final ShortArray3d blocks, final NibbleArray3d blocklight, final NibbleArray3d skylight) {
        this.blocks = blocks;
        this.blocklight = blocklight;
        this.skylight = skylight;
    }

    public ShortArray3d getBlocks() {
        return this.blocks;
    }

    public NibbleArray3d getBlockLight() {
        return this.blocklight;
    }

    public NibbleArray3d getSkyLight() {
        return this.skylight;
    }

    public boolean isEmpty() {
        for (final short block : this.blocks.getData()) {
            if (block != 0) {
                return false;
            }
        }
        return true;
    }
}