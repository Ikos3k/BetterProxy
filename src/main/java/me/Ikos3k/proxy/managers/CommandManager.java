package me.Ikos3k.proxy.managers;

import me.Ikos3k.proxy.commands.CommandHelp;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.objects.Manager;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.ReflectionUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Optional;

public class CommandManager extends Manager<Command> {
    private final HashMap<String, Long> cooldown = new HashMap<>();

    public void init() {
        elements.addAll(ReflectionUtil.getClasses("me.Ikos3k.proxy.commands", Command.class));
    }

    public void onCommand(final String message, final Player sender) {
        final String[] args = message.split(" ");
        Optional<Command> optionalCommand = elements.stream().filter(cmd -> (sender.getPrefixCMD() + cmd.getPrefix()).equalsIgnoreCase(args[0])).findFirst();
        if (!sender.isLogged() && !(message.startsWith(sender.getPrefixCMD() + "login ") || message.startsWith(sender.getPrefixCMD() + "l "))) {
            ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + "You must login! &c" + sender.getPrefixCMD() + "login <password>", sender, true);
            return;
        }
        if (!optionalCommand.isPresent()) {
            optionalCommand = elements.stream().filter(cmd -> cmd.getAlias() != null && (sender.getPrefixCMD() + cmd.getAlias()).equalsIgnoreCase(args[0])).findFirst();
            if (!optionalCommand.isPresent()) {
                ChatUtil.sendChatMessage("&cCommand not found!", sender, true);
                return;
            }
        }

        final Command command = optionalCommand.get();
        final String packageName = command.getClass().getPackage().getName();
        final String commandType = packageName.substring(packageName.lastIndexOf(".")).substring(1);
        if (commandType.equals("admins") && sender.getAccount().getGroup().getPermission() < 2) {
            ChatUtil.sendChatMessage("&cYou are not permitted to this command!", sender, true);
            return;
        }

        if (command.getConnected() == ConnectedType.CONNECTED && !sender.isConnected()) {
            ChatUtil.sendChatMessage("&4You must be connected to the server!", sender, true);
            return;
        }
        if (command.getConnected() == ConnectedType.DISCONNECTED && sender.isConnected()) {
            ChatUtil.sendChatMessage(new BaseComponent[] {
                    new TextComponent(ChatUtil.fixColor("&4You cannot be connected to the server!"))

                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor(sender.getThemeType().getColor(1) + " click to disconnect from server: " + sender.getServerData().getHost()))))
                .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, sender.getPrefixCMD() + "q")) }, sender, true);
            return;
        }

        try {
            if (!(command instanceof CommandHelp) && cooldown.containsKey(sender.getUsername())) {
                long secondsLeft = cooldown.get(sender.getUsername()) / 1000L + sender.getAccount().getGroup().getDelayCMD() - System.currentTimeMillis() / 1000L;
                if (secondsLeft > 0L) {
                    ChatUtil.sendChatMessage("&7The next command can be used in " + sender.getThemeType().getColor(1) + secondsLeft + "s&7!", sender, true);
                    return;
                }
                cooldown.put(sender.getUsername(), System.currentTimeMillis());
            }

            command.onCommand(sender, args);
        } catch (Exception e) {
            ChatUtil.sendChatMessage("&8Correct usage: " + sender.getThemeType().getColor(1) + sender.getPrefixCMD() + command.getPrefix() + " " + command.getUsage(), sender, true);
        }
    }
}