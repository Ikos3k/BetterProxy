package me.Ikos3k.proxy.threads;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.utils.PacketUtil;

import java.util.TimerTask;

public class TitleLagThread extends TimerTask {
    public void run() {
        BetterProxy.getInstance().getPlayerManager().elements.stream()
            .filter(p -> p.isConnected() && p.getOptionsManager().getOptionByName("titlelag").isEnabled() && p.getLastPacket().getLastMs() > 1500)
            .forEach(p -> PacketUtil.sendTitle(p, p.getThemeType().getColor(3) + "Server is not responding!", p.getThemeType().getColor(1) + p.getLastPacket().getLastMs() + "&cms", 2, 15, 4));
    }
}