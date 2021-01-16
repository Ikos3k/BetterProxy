package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Bot;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.Random;

public class CommandSpam extends Command {
    public CommandSpam() {
        super("spam", "spambypass", "spam message or command with bots", "[repeat] [delay] [message]", CommandType.BOTS, ConnectedType.NONE);
    }

    protected String getRandomString(int lenght) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < lenght) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if(sender.getBots().size() == 0){
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }else {
            String[] message = cmd.split("#spam ", 2);
            ChatUtil.sendChatMessage("&7message : " + message[1], sender, true);
            for (Bot bot : sender.getBots()) {
                    String start = getRandomString(1);
                    String firstmiddle = getRandomString(5);
                    String end = getRandomString(1);
                    String endmiddle = getRandomString(5);
                    bot.getSession().sendPacket(new ClientChatPacket(start + "| (" + firstmiddle + ") |" + start + " " + message[1] + " " + end + "| (" + endmiddle + ") |" + end));
            }
        }
    }
}
