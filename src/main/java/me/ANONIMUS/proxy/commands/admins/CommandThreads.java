package me.ANONIMUS.proxy.commands.admins;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.Set;

public class CommandThreads extends Command {
    public CommandThreads() {
        super("threads", null, null, null, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        ChatUtil.sendChatMessage("threads: " + (Thread.activeCount() > 7 ? "&4" : "&e") + Thread.activeCount(), sender, true);
        ChatUtil.sendChatMessage("current: " + sender.getThemeType().getColor(1) + Thread.currentThread(), sender, true);
        ChatUtil.sendChatMessage("-------------------------------------", sender, false);
        threadSet.forEach(thread -> ChatUtil.sendChatMessage(" &7thread: &a" + thread, sender, false));
        ChatUtil.sendChatMessage("-------------------------------------", sender, false);
    }
}