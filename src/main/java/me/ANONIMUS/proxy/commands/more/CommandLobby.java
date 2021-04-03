package me.ANONIMUS.proxy.commands.more;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.WorldUtil;

public class CommandLobby extends Command {
    public CommandLobby() { super("lobby", null, "teleport to lobby", null, ConnectedType.DISCONNECTED); }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        WorldUtil.lobby(sender, false);
        ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Teleported to the lobby!", sender, false);
    }
}