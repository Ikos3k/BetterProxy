package me.ANONIMUS.proxy.commands.admins;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerDisconnectPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandKick extends Command {
    public CommandKick() {
        super("kick", null, null, "[player] [reason]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        String reason = "";
        for (int i = 2; i < args.length; ++i) {
            reason = (i != 2 ? reason + " " : "") + args[i];
        }

        Player player = BetterProxy.getInstance().getPlayerManager().getPlayer(args[1]);
        if (player == null) {
            ChatUtil.sendChatMessage("&cThe specified player is offline!", sender, true);
            return;
        }

        if (sender.getUsername().equals(args[1])) {
            ChatUtil.sendChatMessage("&cYou can't kick yourself out!", sender, true);
            return;
        }

        if (player.getAccount().getGroup().getPermissionLevel() >= sender.getAccount().getGroup().getPermissionLevel()) {
            ChatUtil.sendChatMessage("&cYou cannot kick this player!", sender, true);
            return;
        }

        ChatUtil.sendChatMessage("&7You have successfully kicked a player with a nickname: " + sender.getThemeType().getColor(1) + args[1], sender, true);
        player.getSession().sendPacket(new ServerDisconnectPacket("You have been kicked out, reason: " + reason));
    }
}