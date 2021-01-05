package me.ANONIMUS.proxy.utils.proxy;

import me.ANONIMUS.proxy.protocol.data.TitleAction;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerChatPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerTitlePacket;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.data.MessagePosition;
import me.ANONIMUS.proxy.protocol.objects.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ChatUtil {
    public static String fixColor(final String text) {
        return text.replace('&', '§')
                .replace(">>", "»")
                .replace("<<", "«")
                .replace("(o)", "●")
                .replace("(*)", "•");
    }

    public static void sendHoverMessage(Player player, String s1, String s2) {
        player.getSession().sendPacket(new ServerChatPacket(Component.text(ChatUtil.fixColor(s1)).hoverEvent(HoverEvent.showText(Component.text(ChatUtil.fixColor(s2))))));
    }

    public static void sendTitle(final Player player, final String header, final String footer) {
        if (header != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TITLE, header));
        if (footer != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.SUBTITLE, footer));
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TIMES, 10, 10, 10));
    }

    public static void sendTitle(final Player player, final String header, final String footer, int s, int d, int f) {
        if (header != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TITLE, header));
        if (footer != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.SUBTITLE, footer));
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TIMES, s, d, f));
    }

    public static void sendChatMessage(final String message, Player player, boolean prefix) {
        player.getSession().sendPacket(new ServerChatPacket((prefix ? "&6BetterProxy &8>> " : "") + "&7" + message));
    }

    public static void sendActionBar(final String message, Player player) {
        player.getSession().sendPacket(new ServerChatPacket(message, MessagePosition.HOTBAR));
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

    public static String stripColor(final String string) {
        return Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]").matcher(string).replaceAll("");
    }
}