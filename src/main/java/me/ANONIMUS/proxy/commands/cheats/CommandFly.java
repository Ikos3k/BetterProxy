package me.ANONIMUS.proxy.commands.cheats;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.PacketUtil;

public class CommandFly extends Command {
    public CommandFly() {
        super("fly", null, null, "[true/false]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        PacketUtil.fly(sender.getSession(), Boolean.parseBoolean(args[1]));
        ChatUtil.sendChatMessage("&7Flying was successfully set to " + sender.getThemeType().getColor(1) + args[1] + "&7!", sender, true);
    }
}