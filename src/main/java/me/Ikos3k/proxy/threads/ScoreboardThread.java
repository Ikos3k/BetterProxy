package me.Ikos3k.proxy.threads;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.utils.ScoreboardUtil;

import java.util.TimerTask;

public class ScoreboardThread extends TimerTask {
    public void run() {
        BetterProxy.getInstance().getPlayerManager().elements.forEach(ScoreboardUtil::updateScoreboard);
    }
}