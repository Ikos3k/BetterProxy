package me.ANONIMUS.proxy.commands.more;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.enums.ThemeType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandTheme extends Command {
    public CommandTheme() {
        super("theme", "themes", null, Arrays.toString(ThemeType.values()), ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (args.length == 2) {
            sender.setThemeType(ThemeType.valueOf(args[1].toUpperCase()));
            ChatUtil.sendChatMessage("Successfully changed theme to " + sender.getThemeType().getColor(1) + sender.getThemeType(), sender, true);
            return;
        }

        ChatUtil.sendChatMessage("Current theme: " + sender.getThemeType().getColor(1) + sender.getThemeType(), sender, true);
        ChatUtil.sendChatMessage("Themes: " + Arrays.stream(ThemeType.values()).map(themeType -> themeType.getColor(1) + themeType.name()).collect(Collectors.joining(", ")), sender, true);
    }
}
