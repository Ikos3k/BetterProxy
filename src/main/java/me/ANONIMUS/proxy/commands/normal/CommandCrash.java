package me.ANONIMUS.proxy.commands.normal;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.objects.Exploit;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.stream.Collectors;

public class CommandCrash extends Command {
    public CommandCrash() {
        super("crash", "exploit", ";D", "[method, list] [amount]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (args[1].equals("list") && args.length == 2) {
            ChatUtil.sendChatMessage("&8>> " + sender.getThemeType().getColor(1) + BetterProxy.getInstance().getExploitManager().getExploits().stream().map(Exploit::getName).collect(Collectors.joining(", ")), sender, false);
            return;
        }

        Exploit exploit = BetterProxy.getInstance().getExploitManager().findExploit(args[1]);
        if (exploit == null) {
            ChatUtil.sendChatMessage("&4Unknown method!", sender, true);
            return;
        }

        if (sender.getBots().isEmpty() && !sender.isConnected()) {
            ChatUtil.sendChatMessage("&4You must be connected to the server!", sender, true);
            return;
        }

        try {
            int arguments = exploit.getArguments().split(" ").length;
            Object[] objects = new Object[arguments];
            System.arraycopy(args, 2, objects, 0, arguments);
            exploit.execute(sender, objects);
        } catch (Exception e) {
            ChatUtil.sendChatMessage("&8Correct usage: " + sender.getThemeType().getColor(1) + sender.getPrefixCMD() + getPrefix() + " " + args[1] + " " + exploit.getArguments(), sender, true);
        }
    }
}