package me.Ikos3k.proxy.utils;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

public class NbtUtil {
    public static void toFile(NBTTagCompound compound, File file, CompressionType compression) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        DataOutputStream dos = null;

        switch (compression) {
            case NONE:
                dos = new DataOutputStream(bos);
                break;
            case GZIP:
                dos = new DataOutputStream(new GZIPOutputStream(bos));
                break;
            case ZLIB:
                dos = new DataOutputStream(new DeflaterOutputStream(bos));
                break;
        }

        CompressedStreamTools.write(compound, dos);
        dos.close();
    }

    public static NBTTagCompound fromFile(File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        DataInputStream in = null;

        switch (CompressionType.getCompression(new FileInputStream(file))) {
            case NONE:
                in = new DataInputStream(bis);
                break;
            case GZIP:
                in = new DataInputStream(new GZIPInputStream(bis));
                break;
            case ZLIB:
                in = new DataInputStream(new InflaterInputStream(bis));
                break;
        }

        NBTTagCompound nbt = CompressedStreamTools.read(in);
        in.close();

        return nbt;
    }

    public enum CompressionType {
        NONE, GZIP, ZLIB;

        public static CompressionType getCompression(InputStream in) throws IOException {
            if (!in.markSupported()) {
                in = new BufferedInputStream(in);
            }

            in.mark(0);

            if (in.read() == 120) {
                return ZLIB;
            }

            in.reset();
            if (in.read() == 31) {
                return GZIP;
            }

            return NONE;
        }
    }
}