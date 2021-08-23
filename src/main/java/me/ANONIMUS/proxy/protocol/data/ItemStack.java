package me.ANONIMUS.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemStack {
    private final int id;
    private final int amount;
    private final int data;
    private NBTTagCompound nbt;

    public ItemStack(int id) {
        this.id = id;
        this.amount = 1;
        this.data = 0;
    }

    public ItemStack(int id, int amount) {
        this(id, amount, 0, null);
    }

    public ItemStack(int id, String name) {
        this(id);

        setStackDisplayName(name);
    }

    public ItemStack(int id, int amount, int data) {
        this(id, amount, data, null);
    }

    public ItemStack setStackDisplayName(String displayName) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        if (!this.nbt.hasKey("display", 10)) {
            this.nbt.setTag("display", new NBTTagCompound());
        }

        this.nbt.getCompoundTag("display").setString("Name", displayName);
        return this;
    }

    public ItemStack setLoreName(List<String> lore) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        if (!this.nbt.hasKey("display", 10)) {
            this.nbt.setTag("display", new NBTTagCompound());
        }

        NBTTagList lores = new NBTTagList();

        lore.forEach(l -> lores.appendTag(new NBTTagString(l)));

        this.nbt.getCompoundTag("display").setTag("Lore", lores);
        return this;
    }

    public ItemStack setLoreName(NBTTagList lores) {
        if (this.nbt == null) {
            this.nbt = new NBTTagCompound();
        }

        if (!this.nbt.hasKey("display", 10)) {
            this.nbt.setTag("display", new NBTTagCompound());
        }

        this.nbt.getCompoundTag("display").setTag("Lore", lores);
        return this;
    }

    public String getName() {
        if (this.nbt != null && this.nbt.hasKey("display", 10)) {
            NBTTagCompound nbttagcompound = this.nbt.getCompoundTag("display");

            if (nbttagcompound.hasKey("Name", 8)) {
                return nbttagcompound.getString("Name");
            }
        }

        return null;
    }

    public ItemStack copy() {
        ItemStack itemstack = new ItemStack(this.id, this.amount, this.data, this.nbt);

        if (this.nbt != null) {
            itemstack.nbt = (NBTTagCompound) this.nbt.copy();
        }

        return itemstack;
    }
}
