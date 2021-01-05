package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.TimerTask;

public class MessageThread extends TimerTask {
    public void run() { PlayerManager.getPlayers().forEach(p -> ChatUtil.sendChatMessage("&6>> &8You are on a proxy by &6ANONIMUS&8!", p, false)); }
}