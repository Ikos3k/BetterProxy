package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.utils.ScoreboardUtil;

import java.util.TimerTask;

public class ScoreboardThread extends TimerTask {
    public void run() {
        BetterProxy.getInstance().getPlayerManager().elements.forEach(ScoreboardUtil::updateScoreboard);
    }
}