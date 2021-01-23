package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerAbilitiesPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerListEntryPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerListHeaderFooter;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerSetSlotPacket;

import java.util.stream.IntStream;

public class PacketUtil {
    public static void fly(final Session session, final boolean fly) {
        session.sendPacket(new ServerPlayerAbilitiesPacket(false, fly, fly, fly, 0.1f, 0.1f));
    }

    public static void speed(final Session session, final int speed) {
        session.sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 1.0f, (float) speed));
    }

    public static void clearInventory(final Player player) {
        IntStream.range(0, 45).forEach(i -> player.getSession().sendPacket(new ServerSetSlotPacket(0, i, null)));
    }

    public static void clearTabList(final Player player) {
        PlayerListEntry[] playerListEntries = new PlayerListEntry[player.getTabList().size()];
        for(int i = 0; i < player.getTabList().size(); i++) {
            playerListEntries[i] = player.getTabList().get(i);
        }
        player.getSession().sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, playerListEntries));
        player.getSession().sendPacket(new ServerPlayerListHeaderFooter(" ", " "));
        player.getTabList().clear();
    }
}