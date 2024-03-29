package me.Ikos3k.proxy.commands.admins;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

import java.util.Set;

public class CommandThreads extends Command {
    public CommandThreads() {
        super("threads", null, null, null, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        ChatUtil.sendChatMessage("threads: " + (Thread.activeCount() > 7 ? "&4" : "&e") + Thread.activeCount(), sender, true);
        ChatUtil.sendChatMessage("current: " + sender.getThemeType().getColor(1) + Thread.currentThread(), sender, true);

        ChatUtil.sendChatMessage("-------------------------------------", sender, false);
        threadSet.forEach(thread -> ChatUtil.sendChatMessage(" &7thread: &a" + thread, sender, false));
        ChatUtil.sendChatMessage("-------------------------------------", sender, false);
    }
}