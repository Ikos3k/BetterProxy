package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandPrefix extends Command {
    public CommandPrefix() {
        super("prefix", "cmdprefix", "change command desc", "[prefix]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        sender.setPrefixCMD(args[1]);
        ChatUtil.sendChatMessage("&7Command prefix was set to: " + sender.getThemeType().getColor(1) + sender.getPrefixCMD(), sender, true);
    }
}