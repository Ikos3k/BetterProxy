package me.ANONIMUS.proxy.protocol.data;

import lombok.Data;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

@Data
public class Rotation {
    private final float x;
    private final float y;
    private final float z;

    public NBTTagList writeToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(new NBTTagFloat(this.x));
        nbttaglist.appendTag(new NBTTagFloat(this.y));
        nbttaglist.appendTag(new NBTTagFloat(this.z));
        return nbttaglist;
    }
}