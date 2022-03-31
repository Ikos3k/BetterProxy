package me.Ikos3k.proxy.commands.normal;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.objects.Exploit;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;

import java.util.Optional;
import java.util.stream.Collectors;

public class CommandCrash extends Command {
    public CommandCrash() {
        super("crash", "exploit", ";D", "[method, list] [amount]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (args[1].equals("list") && args.length == 2) {
            ChatUtil.sendChatMessage("&8>> " + sender.getThemeType().getColor(1) + BetterProxy.getInstance().getExploitManager().elements.stream().map(exploit -> exploit.getName() + (exploit.isCompressed() ? sender.getThemeType().getColor(2) + "(compressed)" + sender.getThemeType().getColor(1) : "")).collect(Collectors.joining(", ")), sender, false);
            return;
        }

        if (sender.getBots().isEmpty() && !sender.isConnected()) {
            ChatUtil.sendChatMessage("&4You must be connected to the server!", sender, true);
            return;
        }

        Optional<Exploit> optionalExploit = BetterProxy.getInstance().getExploitManager().findExploit(args[1]);

        if (!optionalExploit.isPresent()) {
            ChatUtil.sendChatMessage("&4Unknown method!", sender, true);
            return;
        }

        Exploit exploit = optionalExploit.get();

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