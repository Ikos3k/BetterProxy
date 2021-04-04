package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.objects.Option;
import me.ANONIMUS.proxy.protocol.data.ItemStack;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerSetSlotPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtil {
    public static ItemStack option(Option option) {
        ItemStack is = new ItemStack(160, 1, option.isEnabled() ? 5 : 14).setStackDisplayName(ChatUtil.fixColor("&l" + option.getName().toUpperCase()));
        final List<String> lores = new ArrayList<>();
        lores.add("");
        if (option.hasDescription()) {
            lores.add(ChatUtil.fixColor("&fDescription: "));
            Arrays.stream(option.getDescription()).forEach(s -> lores.add(ChatUtil.fixColor("&7" + s)));
            lores.add("");
        }
        lores.add(ChatUtil.fixColor("&fClick to " + (option.isEnabled() ? "&cdisable" : "&aenable")));
        return is.setLoreName(lores);
    }

    public static ItemStack optionsMenu() {
        return new ItemStack(404, ChatUtil.fixColor("&8Options"));
    }

    public static ItemStack changeSkinMenu() {
        return new ItemStack(397, 1, 3).setStackDisplayName(ChatUtil.fixColor("&6Skins"));
    }

    public static ItemStack serverMenu() {
        return new ItemStack(2, ChatUtil.fixColor("&aServers"));
    }

    public static void loadStartItems(Player player) {
        player.getSession().sendPacket(new ServerSetSlotPacket(0, 36, serverMenu()));
        player.getSession().sendPacket(new ServerSetSlotPacket(0, 40, optionsMenu()));
        player.getSession().sendPacket(new ServerSetSlotPacket(0, 44, changeSkinMenu()));
    }
}