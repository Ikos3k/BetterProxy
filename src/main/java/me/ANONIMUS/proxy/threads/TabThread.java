package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerListHeaderFooter;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.TimerTask;

/**
 * @author nyatix
 * @created 20.05.2021 - 20:35
 */
public class TabThread extends TimerTask {
    @Override
    public void run() {
        BetterProxy.getInstance().getPlayerManager().getPlayers().stream().filter(Player::isLogged).forEach(player -> player.getSession().sendPacket(new ServerPlayerListHeaderFooter(ChatUtil.fixColor(
            "\n&l" + player.getThemeType().getColor(1) + "BetterProxy" +
                "\n" +
                "\n" +
                "&7Group: " + player.getAccount().getGroup().getPrefix() +
                "\n" +
                "&7Username: " + player.getThemeType().getColor(2) + player.getAccount().getUsername()
            ),
            ChatUtil.fixColor("&7Session: " + player.getThemeType().getColor(2) + (!player.isConnected() ? "Not connected" : player.getServerData().getHost()) + "\n\n" + "&l" + player.getThemeType().getColor(1) + "BetterProxy\n"))
        ));
    }
}