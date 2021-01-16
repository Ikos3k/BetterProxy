package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandMotherDelay extends Command {
    public CommandMotherDelay() {
        super("motherdelay", null, "add delay to #mother", "[delay]", CommandType.BOTS, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        int delay = Integer.parseInt(args[1]);
        sender.setMotherdelay(delay);
    }
}
