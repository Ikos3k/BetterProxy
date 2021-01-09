package me.ANONIMUS.proxy.protocol.objects;

import me.ANONIMUS.proxy.protocol.data.Chunk;

public class ChunkFix {
    public final Chunk[] chunks;
    public final byte[] biomes;
    public final int x, z;

    public ChunkFix(int x, int z, Chunk[] chunks, byte[] biomes) {
        this.x = x;
        this.z = z;
        this.chunks = chunks;
        this.biomes = biomes;
    }

    public void setBlock(int x, int y, int z, int block, byte data) {
        if (x > 0 && x < 16 && z > 0 && z < 16 && y > 0 && y < 256) {
            this.chunks[y / 16].getBlocks().setBlockAndData(x, y % 16, z, block, data);
        }
    }

    public void setBlockLight(int x, int y, int z, int val) {
        if (x > 0 && x < 16 && z > 0 && z < 16 && y > 0 && y < 256) {
            this.chunks[y / 16].getBlockLight().set(x, y, z, val);
        }
    }

    public void setSkyLight(int x, int y, int z, int val) {
        if (x > 0 && x < 16 && z > 0 && z < 16 && y > 0 && y < 256) {
            this.chunks[y / 16].getSkyLight().set(x, y, z, val);
        }
    }

    public int getHighestBlock(int x, int z) {
        for (int i = 15; i >= 0; --i) {
            for (int j = 15; j >= 0; --j) {
                if (this.chunks[i].getBlocks().get(x, j, z) == 0) continue;
                return j;
            }
        }
        return 0;
    }

    public int getBlock(int x, int y, int z) {
        return this.chunks[y / 16].getBlocks().get(x, y % 16, z);
    }
}