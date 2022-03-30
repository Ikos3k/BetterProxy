package me.Ikos3k.proxy.utils;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerChatPacket;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.stream.IntStream;

public class ChatUtil {
    public static String fixColor(String text) {
        return text.replace('&', '\u00A7')
                .replace(">>", "»")
                .replace("<<", "«")
                .replace("(o)", "●")
                .replace("(*)", "•");
    }

    public static void sendHoverMessage(Player player, String s1, String s2) {
        player.getSession().sendPacket(new ServerChatPacket(new TextComponent(fixColor(s1))
            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(fixColor(s2))))));
    }

    public static void sendChatMessage(String message, Player player, boolean prefix) {
        player.getSession().sendPacket(new ServerChatPacket(fixColor((prefix ? player.getThemeType().getColor(1) + "BetterProxy &8>> " : "") + "&7" + message)));
    }

    public static void sendChatMessage(BaseComponent[] text, Player player, boolean prefix) {
        player.getSession().sendPacket(new ServerChatPacket(new ComponentBuilder(fixColor((prefix ? player.getThemeType().getColor(1) + "BetterProxy &8>> " : "") + "&7")).append(text).create()));
    }

    public static void sendBroadcastMessage(String message, boolean prefix) {
        BetterProxy.getInstance().getPlayerManager().elements.forEach(p -> sendChatMessage(message, p, prefix));
    }

    public static void clearChat(int x, Player player) {
        IntStream.range(0, x).forEach(i -> sendChatMessage(" ", player, false));
    }
}