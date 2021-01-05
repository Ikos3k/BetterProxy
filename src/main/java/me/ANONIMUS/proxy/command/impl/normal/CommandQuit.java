package me.ANONIMUS.proxy.command.impl.normal;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.WorldUtil;

public class CommandQuit extends Command {
    public CommandQuit() {
        super("quit", "q", "Disconnecting from server", "", CommandType.NORMAL, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        ChatUtil.sendChatMessage("&6>> &cDisconnected!", sender, false);
        sender.setServerData(null);
        sender.setConnected(false);
        sender.getRemoteSession().getChannel().close();
        sender.setRemoteSession(null);
        WorldUtil.lobby(sender, true);
    }
}
