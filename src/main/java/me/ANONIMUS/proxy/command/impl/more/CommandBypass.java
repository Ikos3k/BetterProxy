package me.ANONIMUS.proxy.command.impl.more;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.enums.BypassType;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;

import java.util.Arrays;

public class CommandBypass extends Command {
    public CommandBypass() { super("bypass", null, null, Arrays.toString(BypassType.values()).toLowerCase(), CommandType.MORE, ConnectedType.DISCONNECTED); }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        sender.setBypassType(BypassType.valueOf(args[1].toUpperCase()));
        ChatUtil.sendChatMessage("&7You have set the bypass to: &6" + args[1].toUpperCase(), sender, true);
    }
}
