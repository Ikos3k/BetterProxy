package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandMother extends Command {
    public CommandMother() {
        super("mother", null, "", "", CommandType.BOTS, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        sender.setMother(!sender.isMother());
        ChatUtil.sendChatMessage("&7Mother is successfully set to &6" + sender.isMother() + "&7!", sender, true);
    }
}