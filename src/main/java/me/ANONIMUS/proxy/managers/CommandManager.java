package me.ANONIMUS.proxy.managers;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CommandManager {
    private final HashMap<String, Long> cooldown = new HashMap<>();
    private final List<Command> commands = new ArrayList<>();

    public void init() {
        new Reflections("me.ANONIMUS.proxy.commands").getSubTypesOf(Command.class).forEach(cmd -> {
            try {
                commands.add(cmd.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public void onCommand(final String message, final Player sender) {
        final String[] args = message.split(" ");
        Optional<Command> optionalCommand = commands.stream().filter(cmd -> (sender.getPrefixCMD() + cmd.getPrefix()).equalsIgnoreCase(args[0])).findFirst();
        if (!sender.isLogged() && !(message.startsWith(sender.getPrefixCMD() + "login ") || message.startsWith(sender.getPrefixCMD() + "l "))) {
            ChatUtil.sendChatMessage("&6You must login! &c" + sender.getPrefixCMD() + "login <password>", sender, true);
            return;
        }
        if (!optionalCommand.isPresent()) {
            optionalCommand = commands.stream().filter(cmd -> cmd.getAlias() != null && (sender.getPrefixCMD() + cmd.getAlias()).equalsIgnoreCase(args[0])).findFirst();
        }
        if (!optionalCommand.isPresent()) {
            ChatUtil.sendChatMessage("&cCommand not found!", sender, true);
            return;
        }

        final Command command = optionalCommand.get();
        final String packageName = command.getClass().getPackage().getName();
        final String commandType = packageName.substring(packageName.lastIndexOf(".")).substring(1);
        if (commandType.equals("admins") && sender.getAccount().getGroup().getPermissionLevel() < 2) {
            ChatUtil.sendChatMessage("&cYou are not permitted to this command!", sender, true);
            return;
        }
        if (command.getConnected() == ConnectedType.CONNECTED && !sender.isConnected()) {
            ChatUtil.sendChatMessage("&4You must be connected to the server!", sender, true);
            return;
        }
        if (command.getConnected() == ConnectedType.DISCONNECTED && sender.isConnected()) {
            ChatUtil.sendChatMessage("&4You cannot be connected to the server!", sender, true);
            return;
        }
        try {
            if (cooldown.containsKey(sender.getAccount().getUsername())) {
                final long secondsLeft = cooldown.get(sender.getAccount().getUsername()) / 1000L + sender.getAccount().getGroup().getDelayCMD() - System.currentTimeMillis() / 1000L;
                if (secondsLeft > 0L) {
                    ChatUtil.sendChatMessage("&7The next command can be used in &6" + secondsLeft + "s&7!", sender, true);
                    return;
                }
            }
            cooldown.put(sender.getAccount().getUsername(), System.currentTimeMillis());
            command.onCommand(sender, args);
        } catch (final Exception e) {
            ChatUtil.sendChatMessage("&8Correct usage: &6" + sender.getPrefixCMD() + command.getPrefix() + " " + command.getUsage(), sender, true);
        }
    }

    public List<Command> getCommands() { return commands; }
}