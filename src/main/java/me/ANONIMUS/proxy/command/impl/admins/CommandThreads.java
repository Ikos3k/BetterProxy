package me.ANONIMUS.proxy.command.impl.admins;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;

import java.util.Set;

public class CommandThreads extends Command {
    public CommandThreads() {
        super("threads", null, null, "", CommandType.ADMINS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        ChatUtil.sendChatMessage("threads: " + (Thread.activeCount() > 7 ? "&4" : "&e") + Thread.activeCount(), sender, true);
        ChatUtil.sendChatMessage("current: &6" + Thread.currentThread(), sender, true);
        ChatUtil.sendChatMessage("-------------------------------------", sender, false);
        threadSet.forEach(thread -> ChatUtil.sendChatMessage(" &7thread: &a" + thread, sender, false));
        ChatUtil.sendChatMessage("-------------------------------------", sender, false);
    }
}