package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.utils.PacketUtil;

import java.util.TimerTask;

public class TitleLagThread extends TimerTask {
    public void run() {
        BetterProxy.getInstance().getPlayerManager().getPlayers().stream().filter(p -> p.isConnected() && p.getOptionsManager().getOptionByName("titlelag").isEnabled() && p.getLastPacket().getLastMs() > 1500).forEach(p -> PacketUtil.sendTitle(p, p.getThemeType().getColor(3) + "Server is not responding!", p.getThemeType().getColor(1) + p.getLastPacket().getLastMs() + "&cms", 2, 15, 4));
    }
}