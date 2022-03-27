package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.PacketUtil;

public class CommandLobby extends Command {
    public CommandLobby() {
        super("lobby", null, "teleport to lobby", null, ConnectedType.DISCONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        PacketUtil.lobbyPosTeleport(sender);
        ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Teleported to the lobby!", sender, false);
    }
}