package me.ANONIMUS.proxy.command.impl.bots;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Bot;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

public class CommandBotQuit extends Command {
    public CommandBotQuit() {
        super("botquit", "botq", null, "[name/all]", CommandType.BOTS, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if (args[1].equals("all")){
            if(sender.getBots().size() == 0){
                ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
                return;
            }
            sender.getBots().forEach(b -> {
                b.getSession().getChannel().close();
                sender.getBots().remove(b);
            });
            ChatUtil.sendChatMessage("&7Successfully kicked out &4" + sender.getBots().size() + " &7bots", sender, true);
            sender.getBots().clear();
        } else {
            Bot b = null;
            for(Bot bot : sender.getBots()) {
                if(bot.getUsername().equals(args[1])) {
                    b = bot;
                }
            }
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