package me.Ikos3k.proxy.commands.cheats;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.PacketUtil;

public class CommandSpeed extends Command {
    public CommandSpeed() {
        super("speed", null, "speed cmd", "[speed]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        PacketUtil.speed(sender.getSession(), Float.parseFloat(args[1]));
        ChatUtil.sendChatMessage("&7Speed was successfully set to " + sender.getThemeType().getColor(1) + args[1] + "&7!", sender, true);
    }
}