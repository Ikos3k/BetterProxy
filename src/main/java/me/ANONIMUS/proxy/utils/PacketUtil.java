package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.*;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class PacketUtil {
    private final static UUID bossBarUUID = UUID.randomUUID();

    public static void fly(final Session session, final boolean fly) {
        session.sendPacket(new ServerPlayerAbilitiesPacket(false, fly, fly, fly, 0.1f, 0.1f));
    }

    public static void speed(final Session session, final int speed) {
        session.sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 1.0f, (float) speed));
    }

    public static void clearInventory(final Player player) {
        IntStream.range(0, 45).forEach(i -> player.getSession().sendPacket(new ServerSetSlotPacket(0, i, null)));
    }

    public static void sendBroadcastPacket(final Packet packet) {
        PlayerManager.getPlayers().forEach(p -> p.getSession().sendPacket(packet));
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

    public static void sendTitle(final Player player, final String header, final String footer) {
        sendTitle(player, header, footer, 10, 10, 10);
    }

    public static void sendTitle(final Player player, final String header, final String footer, final int fadeIn, final int stay, final int fadeOut) {
        if (header != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TITLE, ChatUtil.fixColor(header)));
        if (footer != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.SUBTITLE, ChatUtil.fixColor(footer)));
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TIMES, fadeIn, stay, fadeOut));
    }

    public static void sendActionBar(final String message, final Player player) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_12_2.getProtocol()) {
            player.getSession().sendPacket(new ServerTitlePacket(TitleAction.ACTIONBAR, ChatUtil.fixColor(message)));
        } else {
            player.getSession().sendPacket(new ServerChatPacket(ChatUtil.fixColor(message), MessagePosition.HOTBAR));
        }
    }

    public static void sendBoosBar(final Player player, final String message) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            player.getSession().sendPacket(new ServerSpawnMobPacket(1, (byte) 63, new Position(0, 0, 0), 0, 0, 0, 0, 0, 0, new EntityMetadata(2, MetadataType.STRING, ChatUtil.fixColor(message)), new EntityMetadata(4, MetadataType.BOOLEAN, true)));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(bossBarUUID, 0, ChatUtil.fixColor(message), 1, 0, 0, (byte)0));
        }
    }

    public static void clearBossBar(final Player player) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            player.getSession().sendPacket(new ServerDestroyEntitiesPacket(1));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(bossBarUUID, 1));
        }
    }

    public static List<Protocol> protocols(final int id, final int... protocols) {
        List<Protocol> protocolList = new ArrayList<>();
        for(int protocol : protocols) {
            protocolList.add(new Protocol(id, protocol));
        }
        return protocolList;
    }
}