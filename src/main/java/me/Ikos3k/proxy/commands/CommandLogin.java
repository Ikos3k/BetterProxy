package me.Ikos3k.proxy.commands;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.PacketUtil;
import me.Ikos3k.proxy.utils.WorldUtil;

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

        if (!sender.getAccount().getPassword().equals(args[1])) {
            ChatUtil.sendChatMessage("&cThe given password is not correct!", sender, true);
            return;
        }

        ChatUtil.clearChat(1, sender);
        PacketUtil.clearBossBar(sender);
        ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Your account is valid until: " + sender.getThemeType().getColor(1) + "02-01-2069, 00:00:00&8!", sender, false);
        ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8You will find the available functions under the command: " + sender.getThemeType().getColor(1) + sender.getPrefixCMD() + "help&8!", sender, false);
        sender.setLogged(true);
        WorldUtil.lobby(sender, false);
    }
}