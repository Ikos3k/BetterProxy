package me.Ikos3k.proxy.commands.admins;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.PacketUtil;

public class CommandAlert extends Command {
    public CommandAlert() {
        super("alert", null, null, "[message]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        StringBuilder out = new StringBuilder(args[1]);
        for (int i = 2; i < args.length; ++i) {
            out.append(" ").append(args[i]);
        }

        if (out.toString().equals("")) {
            ChatUtil.sendChatMessage("&cYou cannot send an empty message!", sender, true);
            return;
        }

        BetterProxy.getInstance().getPlayerManager().elements.forEach(player -> PacketUtil.sendTitle(player, "&8[&4ALERT&8]", "&n" + out, 13, 18, 13));
    }
}