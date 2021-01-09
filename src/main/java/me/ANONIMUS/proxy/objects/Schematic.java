package me.ANONIMUS.proxy.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.FileInputStream;
import java.io.IOException;

@Data
@AllArgsConstructor
public class Schematic {
    private final NBTTagCompound nbtTagCompound;

    public Schematic(String file) throws IOException {
        this(CompressedStreamTools.readCompressed(new FileInputStream(file)));
    }
}