package me.Ikos3k.proxy.commands.bots;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.StringUtil;

public class CommandSpam extends Command {
    public CommandSpam() {
        super("spam", "spambypass", "spam message or command with bots", "[repeat] [delay] [message]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (sender.getBots().isEmpty()) {
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }

        ChatUtil.sendChatMessage("&7message : " + args[1], sender, true);
        sender.getBots().forEach(bot -> {
            String start = StringUtil.generateString(1);
            String firstmiddle = StringUtil.generateString(5);
            String end = StringUtil.generateString(1);
            String endmiddle = StringUtil.generateString(5);
            bot.getSession().sendPacket(new ClientChatPacket(start + "| (" + firstmiddle + ") |" + start + " " + args[1] + " " + end + "| (" + endmiddle + ") |" + end));
        });
    }
}