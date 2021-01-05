package me.ANONIMUS.proxy.command.impl;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.proxy.WorldUtil;

public class CommandLogin extends Command {
    public CommandLogin() {
        super("login", "l", null, "[password]", null, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if (sender.isLogged()) {
            ChatUtil.sendChatMessage("&4You are already logged!", sender, true);
            return;
        }
        if (sender.getAccount().getPassword().equals(args[1])) {
            ChatUtil.clearChat(1, sender);
            ChatUtil.sendChatMessage("&6>> &8Your account is valid until: &602-01-2069, 00:00:00&8!", sender, false);
            ChatUtil.sendChatMessage("&6>> &8You will find the available functions under the command: &6" + sender.getPrefixCMD() + "help&8!", sender, false);
            sender.setLogged(true);
            WorldUtil.lobby(sender, false);
            return;
        }
        ChatUtil.sendChatMessage("&cThe given password is not correct!", sender, true);
    }
}