package me.ANONIMUS.proxy.commands.bots;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientChatPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandBotRegister extends Command {
    public CommandBotRegister() {
        super("bregister", "breg", "register bot", "[arguments] [login] [register]", ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        if (sender.getBots().size() == 0) {
            ChatUtil.sendChatMessage("&cYou don't have any connected bots", sender, true);
            return;
        }
        final int arguments = Integer.parseInt(args[1]);
        final boolean login = Boolean.parseBoolean(args[2]);
        final boolean register = Boolean.parseBoolean(args[3]);
        final String passwd = generateString(5 + new Random().nextInt(3));
        final StringBuilder regCMD = new StringBuilder("/register");
        if(arguments > 0) {
            IntStream.range(0, arguments).forEach(i -> regCMD.append(" ").append(passwd));
        }
        sender.getBots().forEach(bot -> {
            if(login) {
                bot.getSession().sendPacket(new ClientChatPacket("/login " + passwd));
            }
            if(register) {
                bot.getSession().sendPacket(new ClientChatPacket(regCMD.toString()));
            }
        });
        ChatUtil.sendChatMessage("&7Successfully register &6(" + sender.getBots().size() + ") &7bots", sender, true);
    }

    public String generateString(int length) {
        final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@#$%&*()".toCharArray();
        return IntStream.range(0, length).mapToObj(i -> Character.toString(chars[new Random().nextInt(chars.length)])).collect(Collectors.joining());
    }
}