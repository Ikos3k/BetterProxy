package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.protocol.data.scoreboard.ObjectiveMode;
import me.ANONIMUS.proxy.protocol.data.scoreboard.ObjectiveType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerDisplayScoreboardPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerScoreboardObjectivePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerUpdateScorePacket;

import java.text.DecimalFormat;

public class ScoreboardUtil {
    private static final DecimalFormat format = new DecimalFormat("##.##");

    public static void sendScoreboard(Player player) {
        String sidebarName = format.format(System.currentTimeMillis());

        if (player.getAccount() != null) {
            player.getSession().sendPacket(new ServerScoreboardObjectivePacket(sidebarName, ObjectiveMode.CREATE, ChatUtil.fixColor(player.getThemeType().getColor(1) + "&lBetterProxy"), ObjectiveType.INTEGER));
            player.getSession().sendPacket(new ServerUpdateScorePacket(ChatUtil.fixColor("&7Nickname: " + player.getThemeType().getColor(1) + player.getUsername()), 0, sidebarName, -1));
            player.getSession().sendPacket(new ServerUpdateScorePacket(ChatUtil.fixColor("&7Group: " + player.getAccount().getGroup().getPrefix()), 0, sidebarName, -2));
            if (player.isConnected() && !player.getUsername().equals(player.getRemoteSession().getUsername())) {
                player.getSession().sendPacket(new ServerUpdateScorePacket(ChatUtil.fixColor("&f<<&m---------------------&r&f>>"), 0, sidebarName, -3));
                player.getSession().sendPacket(new ServerUpdateScorePacket(ChatUtil.fixColor("&7Nickname: " + player.getThemeType().getColor(1) + player.getRemoteSession().getUsername()), 0, sidebarName, -4));
                player.getSession().sendPacket(new ServerUpdateScorePacket(ChatUtil.fixColor("&7Server: " + player.getThemeType().getColor(1) + player.getServerData().getHost()), 0, sidebarName, -5));
            } else if (player.isConnected()) {
                player.getSession().sendPacket(new ServerUpdateScorePacket(ChatUtil.fixColor("&f<<&m---------------------&r&f>>"), 0, sidebarName, -3));
                player.getSession().sendPacket(new ServerUpdateScorePacket(ChatUtil.fixColor("&7Server: " + player.getThemeType().getColor(1) + player.getServerData().getHost()), 0, sidebarName, -4));
            }
        }
        player.getSession().sendPacket(new ServerDisplayScoreboardPacket(1, sidebarName));
    }

    public static void sendEmptyScoreboard(Player player) {
        player.getSession().sendPacket(new ServerScoreboardObjectivePacket("emptySidebar", ObjectiveMode.CREATE, "", ObjectiveType.INTEGER));
        player.getSession().sendPacket(new ServerDisplayScoreboardPacket(1, "emptySidebar"));
    }

    public static void updateScoreboard(Player player) {
        sendEmptyScoreboard(player);

        if(player.getOptionsManager() != null && player.getOptionsManager().getOptionByName("scoreboard").isEnabled()) {
            sendScoreboard(player);
        }
    }
}