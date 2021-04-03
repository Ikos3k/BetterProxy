package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.TimerTask;

public class MessageThread extends TimerTask {
    public void run() { PlayerManager.getPlayers().forEach(p -> ChatUtil.sendChatMessage(p.getThemeType().getColor(1) + ">> &8You are on a proxy by " + p.getThemeType().getColor(1) + "ANONIMUS&8!", p, false)); }
}