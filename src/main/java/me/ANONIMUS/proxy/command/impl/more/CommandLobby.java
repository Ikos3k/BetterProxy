package me.ANONIMUS.proxy.command.impl.more;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.utils.proxy.WorldUtil;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;

public class CommandLobby extends Command {
    public CommandLobby() { super("lobby", null, "teleport to lobby", "", CommandType.MORE, ConnectedType.DISCONNECTED); }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        WorldUtil.lobby(sender, false);
        ChatUtil.sendChatMessage("&6>> &8Teleported to the lobby!", sender, false);
    }
}