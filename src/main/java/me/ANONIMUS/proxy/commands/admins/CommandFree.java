package me.ANONIMUS.proxy.commands.admins;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandFree extends Command {
    public CommandFree() {
        super("free", "clean", "freeing memory", null, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        System.gc();
        System.runFinalization();

        ChatUtil.sendChatMessage("Freeing memory", sender, true);
    }
}