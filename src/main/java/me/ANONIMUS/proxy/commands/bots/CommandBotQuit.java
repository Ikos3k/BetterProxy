package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Bot;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandBotQuit extends Command {
    public CommandBotQuit() {
        super("botquit", "botq", null, "[name/all]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (args[1].equals("all")) {
            if (sender.getBots().isEmpty()) {
                ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
                return;
            }

            sender.getBots().forEach(b -> b.getSession().getChannel().close());
            ChatUtil.sendChatMessage("&7Successfully kicked out &4" + sender.getBots().size() + " &7bots", sender, true);
            sender.getBots().clear();
        } else {
            Bot b = sender.getBots().stream().filter(bot -> bot.getUsername().equals(args[1])).findFirst().orElse(null);

            if (b == null) {
                ChatUtil.sendChatMessage("&cThe bot with a nickname &4" + args[1] + " &cdoes not exist", sender, true);
                return;
            }

            b.getSession().getChannel().close();
            b.setSession(null);
            sender.getBots().remove(b);

            ChatUtil.sendChatMessage("&7A bot with a nickname &4" + args[1] + " &7was successfully thrown out", sender, true);
        }
    }
}