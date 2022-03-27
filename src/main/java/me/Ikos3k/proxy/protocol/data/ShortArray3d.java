package me.Ikos3k.proxy.protocol.data;

import java.util.Arrays;

public class ShortArray3d {
    private final short[] data;

    public ShortArray3d(final int size) {
        this.data = new short[size];
    }

    public ShortArray3d(final short[] array) {
        this.data = array;
    }

    public short[] getData() {
        return this.data;
    }

    public int get(final int x, final int y, final int z) {
        return this.data[y << 8 | z << 4 | x] & 0xFFFF;
    }

    public void set(final int x, final int y, final int z, final int val) {
        this.data[y << 8 | z << 4 | x] = (short) val;
    }

    public int getBlock(final int x, final int y, final int z) {
        return this.get(x, y, z) >> 4;
    }

    public void setBlock(final int x, final int y, final int z, final int block) {
        this.set(x, y, z, block << 4 | this.getData(x, y, z));
    }

    public int getData(final int x, final int y, final int z) {
        return this.get(x, y, z) & 0xF;
    }

    public void setData(final int x, final int y, final int z, final int data) {
        this.set(x, y, z, this.getBlock(x, y, z) << 4 | data);
    }

    public void setBlockAndData(final int x, final int y, final int z, final int block, final int data) {
        this.set(x, y, z, block << 4 | data);
    }

    public void fill(final int val) {
        Arrays.fill(this.data, (short) val);
    }
}