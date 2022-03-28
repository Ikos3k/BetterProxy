package me.Ikos3k.proxy.utils;

import me.Ikos3k.proxy.objects.Option;
import me.Ikos3k.proxy.objects.Skin;
import me.Ikos3k.proxy.protocol.data.ItemStack;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerSetSlotPacket;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtil {
    public static ItemStack option(Option option) {
        List<String> lores = new ArrayList<>();
        lores.add("");
        if (option.hasDescription()) {
            lores.add(ChatUtil.fixColor("&fDescription: "));
            Arrays.stream(option.getDescription()).forEach(s -> lores.add(ChatUtil.fixColor("&7" + s)));
            lores.add("");
        }
        lores.add(ChatUtil.fixColor("&fClick to " + (option.isEnabled() ? "&cdisable" : "&aenable")));
        return new ItemStack(160, 1, option.isEnabled() ? 5 : 14).setName(ChatUtil.fixColor("&l" + option.getName().toUpperCase())).setLoreName(lores);
    }

    public static ItemStack skull(Skin skin) {
        if (skin == null) {
            return new ItemStack(397, 1, 3);
        }

        NBTTagCompound nbt = new NBTTagCompound();

        NBTTagCompound property = new NBTTagCompound();
        property.setString("Signature", skin.getSignature());
        property.setString("Value", skin.getValue());

        NBTTagList textures = new NBTTagList();
        textures.appendTag(property);

        NBTTagCompound properties = new NBTTagCompound();
        properties.setTag("textures", textures);

        NBTTagCompound skullOwner = new NBTTagCompound();
        skullOwner.setString("Id", skin.getGameProfile().getIdAsString());
        skullOwner.setString("Name", skin.getGameProfile().getName());
        skullOwner.setTag("Properties", properties);

        nbt.setTag("SkullOwner", skullOwner);

        return new ItemStack(397, 1, 3, nbt);
    }

    public static ItemStack optionsMenu() {
        return new ItemStack(404, ChatUtil.fixColor("&8Options"));
    }

    public static ItemStack serverMenu() {
        return new ItemStack(2, ChatUtil.fixColor("&aServers"));
    }

    public static ItemStack changeSkinMenu(Player player) {
        return skull(player.getSkin()).setName(ChatUtil.fixColor("&6Skins"));
    }

    public static ItemStack[] playerSkulls(String... nicknames) {
        ItemStack[] itemStacks = new ItemStack[nicknames.length];

        int i = 0;
        for (String nick : nicknames) {
            itemStacks[i] = skull(SkinUtil.getSkin(ChatColor.stripColor(nick))).setName(ChatUtil.fixColor(nick));
//            itemStacks[i] = skull(SkinUtil.getSkin(ChatColor.stripColor(nick)));
            i++;
        }

        return itemStacks;
    }

    public static void loadStartItems(Player player) {
        player.getSession().sendPacket(new ServerSetSlotPacket(0, 36, serverMenu()));
        player.getSession().sendPacket(new ServerSetSlotPacket(0, 40, optionsMenu()));
        player.getSession().sendPacket(new ServerSetSlotPacket(0, 44, changeSkinMenu(player)));
    }
}