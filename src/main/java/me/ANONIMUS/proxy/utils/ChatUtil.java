package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.*;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;
import java.util.stream.IntStream;

public class ChatUtil {
    public static String fixColor(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text
            .replace(">>", "»")
            .replace("<<", "«")
            .replace("(o)", "●")
            .replace("(*)", "•"));
    }

    public static void sendActionBar(final String message, Player player) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_12_2.getProtocol()) {
            player.getSession().sendPacket(new ServerTitlePacket(TitleAction.ACTIONBAR, fixColor(message)));
        } else {
            player.getSession().sendPacket(new ServerChatPacket(fixColor(message), MessagePosition.HOTBAR));
        }
    }

    public static void sendBoosBar(final Player player, final String message) {
        if (player.getSession().getProtocolID() <= ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            player.getSession().sendPacket(new ServerSpawnMobPacket(1, (byte) 63, new Position(0, 0, 0), 0, 0, 0, 0, 0, 0, new EntityMetadata[]{ new EntityMetadata(2, MetadataType.STRING, ChatUtil.fixColor(message)), new EntityMetadata(4, MetadataType.BOOLEAN, true) }));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(UUID.fromString("a02b04d4-3a81-431b-a4cb-aa0df733af00"), 0, ChatUtil.fixColor(message), 1, 0, 0, (byte)0));
        }
    }

    public static void clearBossBar(final Player player) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            player.getSession().sendPacket(new ServerDestroyEntitiesPacket(1));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(UUID.fromString("a02b04d4-3a81-431b-a4cb-aa0df733af00"), 1));
        }
    }

    public static void sendHoverMessage(final Player player, final String s1, final String s2) {
        TextComponent msg = new TextComponent(fixColor(s1));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(fixColor(s2))));
        player.getSession().sendPacket(new ServerChatPacket(msg));
    }

    public static void sendTitle(final Player player, final String header, final String footer) {
        sendTitle(player, header, footer, 10, 10, 10);
    }

    public static void sendTitle(final Player player, final String header, final String footer, int fadeIn, int stay, int fadeOut) {
        if (header != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TITLE, fixColor(header)));
        if (footer != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.SUBTITLE, fixColor(footer)));
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TIMES, fadeIn, stay, fadeOut));
    }

    public static void sendChatMessage(final String message, Player player, boolean prefix) {
        player.getSession().sendPacket(new ServerChatPacket(fixColor((prefix ? player.getThemeType().getColor(1) + "BetterProxy &8>> " : "") + "&7" + message)));
    }

    public static void sendBroadcastMessage(final String message, boolean prefix) {
        PlayerManager.getPlayers().forEach(p -> sendChatMessage(message, p, prefix));
    }

    public static void sendBroadcastPacket(final Packet packet) {
        PlayerManager.getPlayers().forEach(p -> p.getSession().sendPacket(packet));
    }

    public static void clearChat(final int x, final Player player) {
        IntStream.range(0, x).forEach(i -> sendChatMessage(" ", player, false));
    }
}