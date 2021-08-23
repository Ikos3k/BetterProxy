package me.ANONIMUS.proxy.commands.more;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.objects.Skin;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.SkinUtil;

public class CommandSkin extends Command {
    public CommandSkin() {
        super("skin", "changeskin", null, "[player/default]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        final String player = args[1];

        if (player.equalsIgnoreCase("default")) {
            sender.setSkin(SkinUtil.getSkin(sender.getUsername()));
            ChatUtil.sendChatMessage("&7Skin has been successfully reset", sender, true);
            ChatUtil.sendChatMessage("&cYou need to reconnect to the proxy!", sender, true);
            return;
        }

        Skin skin = SkinUtil.getSkin(player);
        if (skin == null) {
            ChatUtil.sendChatMessage("&7The player does not have a premium account", sender, true);
            return;
        }

        sender.setSkin(skin);
        ChatUtil.sendChatMessage("&7You have successfully set skin to " + sender.getThemeType().getColor(1) + args[1], sender, true);
        ChatUtil.sendChatMessage("&cYou need to reconnect to the proxy!", sender, true);
    }
}