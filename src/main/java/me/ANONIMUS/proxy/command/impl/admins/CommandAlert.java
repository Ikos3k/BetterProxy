package me.ANONIMUS.proxy.command.impl.admins;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;

public class CommandAlert extends Command {
    public CommandAlert() {
        super("alert", null, null, "[message]", CommandType.ADMINS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        String out = "";
        for (int i = 1; i < args.length; ++i) { out = (!out.equals("") ? out + " " : "") + args[i]; }
        if(out.equals("")) {
            ChatUtil.sendChatMessage("&cYou cannot send an empty message!", sender, true);
            return;
        }
        String finalOut = out;
        PlayerManager.getPlayers().forEach(player -> ChatUtil.sendTitle(player, "&8[&4ALERT&8]", "&n" + finalOut, 13, 18, 13));
    }
}