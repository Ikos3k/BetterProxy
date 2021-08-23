package me.ANONIMUS.proxy.protocol.data.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.ANONIMUS.proxy.protocol.data.NibbleArray3d;
import me.ANONIMUS.proxy.protocol.data.ShortArray3d;

@Data
@AllArgsConstructor
public class Chunk {
    private final ShortArray3d blocks;
    private final NibbleArray3d blocklight;
    private final NibbleArray3d skylight;

    public Chunk(final boolean skylight) {
        this(new ShortArray3d(4096), new NibbleArray3d(4096), skylight ? new NibbleArray3d(4096) : null);
    }

    public boolean isEmpty() {
        for (short block : this.blocks.getData()) {
            if (block != 0) {
                return false;
            }
        }
        return true;
    }
}