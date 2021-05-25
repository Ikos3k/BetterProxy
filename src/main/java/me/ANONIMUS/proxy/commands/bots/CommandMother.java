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
    public void onCommand(Player sender, String[] args) {
        sender.setMother(!sender.isMother());
        ChatUtil.sendChatMessage("&7Mother was set to: " + sender.getThemeType().getColor(1) + sender.isMother(), sender, true);
        ChatUtil.sendChatMessage("&7set mother delay: " + sender.getThemeType().getColor(2) + sender.getPrefixCMD() + "motherdelay [delay]", sender, true);
    }
}