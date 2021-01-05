package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;

import java.util.TimerTask;

public class TitleLagThread extends TimerTask {
    public void run() { PlayerManager.getPlayers().stream().filter(player -> player.isConnected() && player.getLastPacket().getLastMs() > 1500).forEach(p -> ChatUtil.sendTitle(p, "&4Server is not responding!", "&6" + p.getLastPacket().getLastMs() + "&cms", 2, 15, 4)); }
}