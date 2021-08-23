package me.ANONIMUS.proxy.threads;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.TimerTask;

public class MessageThread extends TimerTask {
    public void run() {
        BetterProxy.getInstance().getPlayerManager().elements.forEach(p -> ChatUtil.sendChatMessage(p.getThemeType().getColor(1) + ">> &8You are on a proxy created by " + p.getThemeType().getColor(1) + "Ikos3k&8!", p, false));
    }
}