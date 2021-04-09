package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerChatPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.stream.IntStream;

public class ChatUtil {
    public static String fixColor(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text
            .replace(">>", "»")
            .replace("<<", "«")
            .replace("(o)", "●")
            .replace("(*)", "•"));
    }

    public static void sendHoverMessage(final Player player, final String s1, final String s2) {
        TextComponent msg = new TextComponent(fixColor(s1));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(fixColor(s2))));
        player.getSession().sendPacket(new ServerChatPacket(msg));
    }

    public static void sendChatMessage(final String message, final Player player, final boolean prefix) {
        player.getSession().sendPacket(new ServerChatPacket(fixColor((prefix ? player.getThemeType().getColor(1) + "BetterProxy &8>> " : "") + "&7" + message)));
    }

    public static void sendBroadcastMessage(final String message, final boolean prefix) {
        PlayerManager.getPlayers().forEach(p -> sendChatMessage(message, p, prefix));
    }

    public static void clearChat(final int x, final Player player) {
        IntStream.range(0, x).forEach(i -> sendChatMessage(" ", player, false));
    }
}