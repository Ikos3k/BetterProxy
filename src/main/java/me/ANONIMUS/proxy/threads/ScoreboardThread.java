package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.utils.ScoreboardUtil;

import java.util.TimerTask;

public class ScoreboardThread extends TimerTask {
    public void run() {
        PlayerManager.getPlayers().forEach(p -> {
            if(p.getOptionsManager() != null && p.getOptionsManager().getOptionByName("scoreboard").isEnabled()) {
                ScoreboardUtil.updateScoreboard(p);
            } else {
                ScoreboardUtil.sendEmptyScoreboard(p);
            }
        });
    }
}