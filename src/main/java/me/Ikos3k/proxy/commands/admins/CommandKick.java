package me.Ikos3k.proxy.commands.admins;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerDisconnectPacket;
import me.Ikos3k.proxy.utils.ChatUtil;

import java.util.Optional;

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

        Optional<Player> optionalPlayer = BetterProxy.getInstance().getPlayerManager().getPlayer(args[1]);
        if (!optionalPlayer.isPresent()) {
            ChatUtil.sendChatMessage("&cThe specified player is offline!", sender, true);
            return;
        }

        Player player = optionalPlayer.get();

        if (sender.getUsername().equals(args[1])) {
            ChatUtil.sendChatMessage("&cYou can't kick yourself out!", sender, true);
            return;
        }

        if (player.getAccount().getGroup().getPermission() >= sender.getAccount().getGroup().getPermission()) {
            ChatUtil.sendChatMessage("&cYou cannot kick this player!", sender, true);
            return;
        }

        ChatUtil.sendChatMessage("&7You have successfully kicked a player with a nickname: " + sender.getThemeType().getColor(1) + args[1], sender, true);
        player.getSession().sendPacket(new ServerDisconnectPacket("You have been kicked out, reason: " + reason));
    }
}