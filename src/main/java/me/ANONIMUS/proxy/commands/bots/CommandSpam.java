package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.Random;

public class CommandSpam extends Command {
    public CommandSpam() {
        super("spam", "spambypass", "spam message or command with bots", "[repeat] [delay] [message]", ConnectedType.NONE);
    }

    private String getRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        if(sender.getBots().size() == 0) {
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }
        ChatUtil.sendChatMessage("&7message : " + args[1], sender, true);
        sender.getBots().forEach(bot -> {
            String start = getRandomString(1);
            String firstmiddle = getRandomString(5);
            String end = getRandomString(1);
            String endmiddle = getRandomString(5);
            bot.getSession().sendPacket(new ClientChatPacket(start + "| (" + firstmiddle + ") |" + start + " " + args[1] + " " + end + "| (" + endmiddle + ") |" + end));
        });
    }
}