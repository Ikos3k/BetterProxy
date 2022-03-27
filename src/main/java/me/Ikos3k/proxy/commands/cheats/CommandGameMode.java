package me.Ikos3k.proxy.commands.cheats;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.data.Gamemode;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.PacketUtil;

public class CommandGameMode extends Command {
    public CommandGameMode() {
        super("gamemode", "gm", null, "[survival|0/" + "creative|1/" + "adventure|2/" + "spectator|3]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        Gamemode gamemode;
        try {
            gamemode = Gamemode.getById(Integer.parseInt(args[1]));
        } catch (Exception e) {
            e.printStackTrace();
            gamemode = Gamemode.valueOf(args[1].toUpperCase());
        }

        PacketUtil.changeGameMode(sender, gamemode);
        ChatUtil.sendChatMessage("&7Gamemode was set to: " + sender.getThemeType().getColor(1) + gamemode.name(), sender, true);
    }
}