package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandMother extends Command {
    public CommandMother() {
        super("mother", null, null, null, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        sender.setMother(!sender.isMother());
        ChatUtil.sendChatMessage("Mother was set to : " + sender.isMother(), sender, true);
        ChatUtil.sendChatMessage("", sender, true);
        ChatUtil.sendChatMessage("add mother delay : " + sender.getPrefixCMD() + "motherdelay [delay]", sender, true);
    }
}