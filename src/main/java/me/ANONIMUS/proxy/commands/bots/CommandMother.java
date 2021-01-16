package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandMother extends Command {
    public CommandMother() {
        super("mother", null, "", "", CommandType.BOTS, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        sender.setMother(!sender.isMother());
        sender.setMotherdelay(0);
        ChatUtil.sendChatMessage("Mother was set to : " + String.valueOf(sender.isMother()), sender, true);
        ChatUtil.sendChatMessage("", sender, true);
        ChatUtil.sendChatMessage("add mother delay : #motherdelay [delay]", sender, true);
    }
}