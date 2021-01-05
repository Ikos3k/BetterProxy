package me.ANONIMUS.proxy.command.impl.more;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.TimeType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerTimeUpdatePacket;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;

import java.util.Arrays;

public class CommandTime extends Command {
    public CommandTime() {
        super("time", "alwaystime", null, Arrays.toString(TimeType.values()).toLowerCase(), CommandType.MORE, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        sender.setTimeType(TimeType.valueOf(args[1].toUpperCase()));
        ChatUtil.sendChatMessage("&7You have set the game time to: &6" + args[1].toUpperCase(), sender, true);
        sender.getSession().sendPacket(new ServerTimeUpdatePacket(sender.getTimeType().getAge(), sender.getTimeType().getTime()));
    }
}