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

    public static void sendHotBar(final String message, Player player) {
        player.getSession().sendPacket(new ServerChatPacket(message, MessagePosition.HOTBAR));
    }

    public static void sendActionBar(final String message, Player player) {
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.ACTIONBAR, message));
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

    public static int getStringWidth(final String s) {
        int width = 0;
        for(char c : s.toCharArray()) {
            width += getCharWidth(c);
        }
        return width;
    }

    public static int getCharWidth(final char c) {
        switch (c) {
            case '`': {
                return 0;
            }
            case '|':
            case 'i': {
                return 2;
            }
            case 'l': {
                return 3;
            }
            case 't':
            case 'I':
            case ' ': {
                return 4;
            }
            case 'k':
            case '\"':
            case '*':
            case 'f': {
                return 5;
            }
            case 'm':
            case 'n':
            case 'o':
            case 'j':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'g':
            case 'h':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '+':
            case '&':
            case '#':
            case '_':
            case '$':
            case '%': {
                return 6;
            }
            case '~':
            case '@': {
                return 7;
            }
        }
        return 1;
    }
}