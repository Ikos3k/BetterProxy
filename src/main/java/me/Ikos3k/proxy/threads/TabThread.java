package me.Ikos3k.proxy.threads;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerPlayerListHeaderFooter;
import me.Ikos3k.proxy.utils.ChatUtil;

import java.util.TimerTask;

/**
 * @author nyatix
 * @created 20.05.2021 - 20:35
 */
public class TabThread extends TimerTask {
    public void run() {
       BetterProxy.getInstance().getPlayerManager().elements.stream().filter(Player::isLogged).forEach(player -> player.getSession().sendPacket(new ServerPlayerListHeaderFooter(ChatUtil.fixColor(
            "\n&l" + player.getThemeType().getColor(1) + "BetterProxy" +
                "\n" +
                "\n" +
                "&7Group: " + player.getAccount().getGroup().getPrefix() +
                "\n" +
                "&7Username: " + player.getThemeType().getColor(2) + player.getUsername()
            ),
            ChatUtil.fixColor("&7Session: " + player.getThemeType().getColor(2) + (!player.isConnected() ? "Not connected" : player.getServerData().getHost()) + "\n\n" + "&l" + player.getThemeType().getColor(1) + "BetterProxy\n"))
        ));
    }
}