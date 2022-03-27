package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandHidePlayers extends Command {
    public CommandHidePlayers() {
        super("hideplayers", null, null, null, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        sender.setHidePlayers(!sender.isHidePlayers());
        ChatUtil.sendChatMessage("&7Hide players was successfully set to " + sender.getThemeType().getColor(1) + sender.isHidePlayers() + "&7!", sender, true);
    }
}