package me.Ikos3k.proxy.commands.admins;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

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