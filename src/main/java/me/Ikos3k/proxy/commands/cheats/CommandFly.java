package me.Ikos3k.proxy.commands.cheats;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.PacketUtil;

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