package me.ANONIMUS.proxy.protocol.data;

import lombok.Data;

@Data
public class NibbleArray3d {
    private final byte[] data;

    public NibbleArray3d(final int size) {
        this.data = new byte[size >> 1];
    }

    public byte[] getData() {
        return this.data;
    }

    public int get(final int x, final int y, final int z) {
        final int key = y << 8 | z << 4 | x;
        final int index = key >> 1;
        final int part = key & 0x1;
        return (part == 0) ? (this.data[index] & 0xF) : (this.data[index] >> 4 & 0xF);
    }

    public void set(final int x, final int y, final int z, final int val) {
        final int key = y << 8 | z << 4 | x;
        final int index = key >> 1;
        final int part = key & 0x1;
        if (part == 0) {
            this.data[index] = (byte) ((this.data[index] & 0xF0) | (val & 0xF));
        } else {
            this.data[index] = (byte) ((this.data[index] & 0xF) | (val & 0xF) << 4);
        }
    }

    public void fill(final int val) {
        for (int index = 0; index < this.data.length << 1; ++index) {
            final int ind = index >> 1;
            final int part = index & 0x1;
            if (part == 0) {
                this.data[ind] = (byte) ((this.data[ind] & 0xF0) | (val & 0xF));
            } else {
                this.data[ind] = (byte) ((this.data[ind] & 0xF) | (val & 0xF) << 4);
            }
        }
    }
}