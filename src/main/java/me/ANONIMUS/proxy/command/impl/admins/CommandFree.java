package me.ANONIMUS.proxy.command.impl.admins;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandFree extends Command {
    public CommandFree() {
        super("free", "clean", "freeing memory", null, CommandType.ADMINS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        System.gc();
        System.runFinalization();
        ChatUtil.sendChatMessage("Freeing memory", sender, true);
    }
}