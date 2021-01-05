package me.ANONIMUS.proxy.command.impl.admins;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;

public class CommandKick extends Command {
    public CommandKick() {
        super("kick", null, null, "[player] [reason]", CommandType.ADMINS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        String text = "";
        for (int i = 2; i < args.length; ++i) { text = (i != 2 ? text + " " : "") + args[i]; }
        Player player;
        player = PlayerManager.getPlayer(args[1]);
        if (player == null) {
            ChatUtil.sendChatMessage("&cThe specified player is offline!", sender, true);
            return;
        }
        if (sender.getAccount().getUsername().equals(args[1])) {
            ChatUtil.sendChatMessage("&cYou can't kick yourself out!", sender, true);
            return;
        }
        if(player.getAccount().getGroup().getPermissionLevel() >= sender.getAccount().getGroup().getPermissionLevel()){
            ChatUtil.sendChatMessage("&cYou cannot kick this player!", sender, true);
            return;
        }
        ChatUtil.sendChatMessage("&7You have successfully kicked a player with a nickname: &5" + args[1], sender, true);
    }
}
