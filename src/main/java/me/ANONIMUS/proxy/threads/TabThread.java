package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.protocol.objects.Player;

import java.util.TimerTask;

/**
 * @author nyatix
 * @created 20.05.2021 - 20:35
 */
public class TabThread extends TimerTask {
    @Override
    public void run() {
        BetterProxy.getInstance().getPlayerManager().getPlayers().stream().filter(Player::isLogged).forEach(Player::updateTab);
    }
}
