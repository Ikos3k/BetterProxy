package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.SkinUtil;

public class CommandSkin extends Command {
    public CommandSkin() {
        super("skin", "changeskin", null, "[player/default]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        final String player = args[1];

        if (player.equalsIgnoreCase("default")) {
            ChatUtil.sendChatMessage("&7Skin has been successfully reset", sender, true);
            sender.updateSkin(SkinUtil.getSkin(sender.getUsername()));
            return;
        }

        sender.updateSkin(SkinUtil.getSkin(player));
    }
}