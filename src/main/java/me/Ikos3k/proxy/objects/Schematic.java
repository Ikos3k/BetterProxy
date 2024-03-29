package me.Ikos3k.proxy.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Ikos3k.proxy.protocol.data.NibbleArray3d;
import me.Ikos3k.proxy.protocol.data.ShortArray3d;
import me.Ikos3k.proxy.protocol.data.chunk.Chunk;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerChunkDataPacket;
import me.Ikos3k.proxy.utils.NbtUtil;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public class Schematic {
    private final NBTTagCompound nbt;
    private final int posX, posZ;

    public Schematic(File f, int posX, int posZ) throws IOException {
        this(NbtUtil.fromFile(f), posX, posZ);
    }

    public void load(Player player) {
        int width = nbt.getShort("Width");
        int height = nbt.getShort("Height");
        int length = nbt.getShort("Length");
        byte[] blocksBytes = nbt.getByteArray("Blocks");
        byte[] dataBytes = nbt.getByteArray("Data");

        Chunk[] chunks = new Chunk[16];

        byte[] biomes = new byte[256];
        Arrays.fill(biomes, (byte) ((Math.sin(posX * posZ) + 1.0D) * 10.0D));

        ShortArray3d blocks = new ShortArray3d(4096);
        NibbleArray3d blockLight = new NibbleArray3d(4096);
        NibbleArray3d skylight = new NibbleArray3d(4096);

        skylight.fill(15);

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                for (int y = 0; y < height; y++) {
                    int blockIndex = y * width * length + z * width + x;
                    blocks.setBlockAndData(x, y, z, blocksBytes[blockIndex], dataBytes[blockIndex]);
                }
            }
        }

        chunks[0] = new Chunk(blocks, blockLight, skylight);
        player.getSession().sendPacket(new ServerChunkDataPacket(posX, posZ, chunks, biomes));
    }
}