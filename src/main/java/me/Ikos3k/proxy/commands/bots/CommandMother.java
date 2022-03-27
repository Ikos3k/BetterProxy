package me.Ikos3k.proxy.commands.bots;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandMother extends Command {
    public CommandMother() {
        super("mother", null, null, null, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        sender.setMother(!sender.isMother());
        ChatUtil.sendChatMessage("&7Mother was set to: " + sender.getThemeType().getColor(1) + sender.isMother(), sender, true);
        if (sender.isMother()) {
            ChatUtil.sendChatMessage("&7Set mother delay: " + sender.getThemeType().getColor(2) + sender.getPrefixCMD() + "motherdelay [delay]", sender, true);
        }
    }
}