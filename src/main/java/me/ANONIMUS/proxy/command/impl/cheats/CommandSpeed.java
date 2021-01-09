package me.ANONIMUS.proxy.command.impl.cheats;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.PacketUtil;

public class CommandSpeed extends Command {
    public CommandSpeed() {
        super("speed", null, "speed cmd", "[speed]", CommandType.CHEATS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        PacketUtil.speed(sender.getSession(), Integer.parseInt(args[1]));
        ChatUtil.sendChatMessage("&7Speed is successfully set to &6" + args[1] + "&7!", sender, true);
    }
}