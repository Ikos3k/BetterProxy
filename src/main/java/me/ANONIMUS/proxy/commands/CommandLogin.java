package me.ANONIMUS.proxy.commands;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.PacketUtil;
import me.ANONIMUS.proxy.utils.WorldUtil;

public class CommandLogin extends Command {
    public CommandLogin() {
        super("login", "l", null, "[password]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (sender.isLogged()) {
            ChatUtil.sendChatMessage("&4You are already logged!", sender, true);
            return;
        }

        if (sender.getAccount().getPassword().equals(args[1])) {
            ChatUtil.clearChat(1, sender);
            PacketUtil.clearBossBar(sender);
            ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Your account is valid until: " + sender.getThemeType().getColor(1) + "02-01-2069, 00:00:00&8!", sender, false);
            ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8You will find the available functions under the command: " + sender.getThemeType().getColor(1) + sender.getPrefixCMD() + "help&8!", sender, false);
            sender.setLogged(true);
            WorldUtil.lobby(sender, false);
            return;
        }

        ChatUtil.sendChatMessage("&cThe given password is not correct!", sender, true);
    }
}