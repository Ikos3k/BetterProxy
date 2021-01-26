package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.data.MessagePosition;
import me.ANONIMUS.proxy.protocol.data.TitleAction;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerChatPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerTitlePacket;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.stream.IntStream;

public class ChatUtil {
    public static String fixColor(final String text) {
        return text.replace('&', '§')
                .replace(">>", "»")
                .replace("<<", "«")
                .replace("(o)", "●")
                .replace("(*)", "•");
    }

    public static void sendHoverMessage(final Player player, final String s1, final String s2) {
        TextComponent msg = new TextComponent(ChatUtil.fixColor(s1));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor(s2))));
        player.getSession().sendPacket(new ServerChatPacket(msg));
    }

    public static void sendTitle(final Player player, final String header, final String footer) {
        sendTitle(player, header, footer, 10, 10, 10);
    }

    public static void sendTitle(final Player player, final String header, final String footer, int fadeIn, int stay, int fadeOut) {
        if (header != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TITLE, ChatUtil.fixColor(header)));
        if (footer != null) player.getSession().sendPacket(new ServerTitlePacket(TitleAction.SUBTITLE, ChatUtil.fixColor(footer)));
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TIMES, fadeIn, stay, fadeOut));
    }

    public static void sendChatMessage(final String message, Player player, boolean prefix) {
        player.getSession().sendPacket(new ServerChatPacket(ChatUtil.fixColor((prefix ? "&6BetterProxy &8>> " : "") + "&7" + message)));
    }

    public static void sendHotBar(final String message, Player player) {
        player.getSession().sendPacket(new ServerChatPacket(ChatUtil.fixColor(message), MessagePosition.HOTBAR));
    }

    public static void sendActionBar(final String message, Player player) {
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.ACTIONBAR, ChatUtil.fixColor(message)));
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