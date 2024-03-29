package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.enums.TimeType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerTimeUpdatePacket;
import me.Ikos3k.proxy.utils.ChatUtil;

import java.util.Arrays;

public class CommandTime extends Command {
    public CommandTime() {
        super("time", "alwaystime", null, Arrays.toString(TimeType.values()).toLowerCase(), ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        sender.setTimeType(TimeType.valueOf(args[1].toUpperCase()));
        ChatUtil.sendChatMessage("&7You have set the game time to: " + sender.getThemeType().getColor(1) + args[1].toUpperCase(), sender, true);

        if (sender.getTimeType() != TimeType.DEFAULT) {
            sender.getSession().sendPacket(new ServerTimeUpdatePacket(sender.getTimeType().getAge(), sender.getTimeType().getTime()));
        }
    }
}