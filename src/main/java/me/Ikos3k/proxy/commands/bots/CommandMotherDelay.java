package me.Ikos3k.proxy.commands.bots;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandMotherDelay extends Command {
    public CommandMotherDelay() {
        super("motherdelay", null, "add delay to #mother", "[delay]", ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        sender.setMotherDelay(Integer.parseInt(args[1]));
        ChatUtil.sendChatMessage("&7Mother delay was set to: " + sender.getThemeType().getColor(1) + args[1], sender, true);
    }
}