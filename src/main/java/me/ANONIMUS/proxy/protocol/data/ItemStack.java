package me.ANONIMUS.proxy.protocol.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.List;

public class ItemStack {
    private final int id;
    private final int amount;
    private final int data;
    private NBTTagCompound nbt;

    public ItemStack(final int id) {
        this.id = id;
        this.amount = 1;
        this.data = 0;
    }

    public ItemStack(final int id, final int amount) {
        this(id, amount, 0, null);
    }

    public ItemStack(final int id, final String name) {
        this(id);

        setStackDisplayName(name);
    }

    public ItemStack(final int id, final int amount, final int data) {
        this(id, amount, data, null);
    }

    public ItemStack(final int id, final int amount, final int data, final NBTTagCompound nbt) {
        this.id = id;
        this.amount = amount;
        this.data = data;
        this.nbt = nbt;
    }

    public int getId() {
        return this.id;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getData() {
        return this.data;
    }

    public NBTTagCompound getNBT() {
        return this.nbt;
    }

    public void setNbt(NBTTagCompound nbt) {
        this.nbt = nbt;
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
