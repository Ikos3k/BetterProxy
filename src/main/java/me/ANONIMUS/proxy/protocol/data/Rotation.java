package me.ANONIMUS.proxy.protocol.data;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotation {
    protected final float x;

    protected final float y;

    protected final float z;

    public Rotation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public NBTTagList writeToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(new NBTTagFloat(this.x));
        nbttaglist.appendTag(new NBTTagFloat(this.y));
        nbttaglist.appendTag(new NBTTagFloat(this.z));
        return nbttaglist;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }
}