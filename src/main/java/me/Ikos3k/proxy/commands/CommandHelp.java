package me.Ikos3k.proxy.commands;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerChatPacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.StringUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Comparator;
import java.util.List;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help", null, "helpful commands", "[page]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        List<Command> list = BetterProxy.getInstance().getCommandManager().elements;
        list.sort(Comparator.comparingInt(s -> StringUtil.getStringWidth(s.getPrefix())));

        int i = 1;
        if (args.length == 2) {
            i = Integer.parseInt(args[1]);
        }

        int k = i - 1;
        int l = Math.min(i * 10, list.size());
        int j = (list.size() - 1) / 10;
        int f = j + 1;

        if (i < 1) {
            ChatUtil.sendChatMessage("&cThe given number (" + i + ") is too small, must be at least 1", sender, false);
            return;
        }

        if (i > f) {
            ChatUtil.sendChatMessage("&cThe given number (" + i + ") is too high, cannot exceed " + f, sender, false);
            return;
        }

        ChatUtil.clearChat(1, sender);
        ChatUtil.sendChatMessage(" &8 --- Showing help page: " + sender.getThemeType().getColor(1) + i + " &8of " + sender.getThemeType().getColor(1) + f + " &8---", sender, false);
        for (int i1 = k * 10; i1 < l; ++i1) {
            final Command command = list.get(i1);
            ChatUtil.sendHoverMessage(sender, "&8>> &f" + sender.getPrefixCMD() + command.getPrefix() + " &7" + command.getUsage(), sender.getThemeType().getColor(1) + command.getDesc());
        }

        TextComponent msg = new TextComponent(ChatUtil.fixColor(((i != 1) ? "&c" : "&7") + "[PREVIOUS]"));
        if (i != 1) {
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor(sender.getThemeType().getColor(2) + "Click to go to the previous page!"))));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, sender.getPrefixCMD() + "help " + k));
        }

        TextComponent msg2 = new TextComponent(ChatUtil.fixColor((i != f ? "&a" : "&7") + "[NEXT]"));
        if (i != f) {
            msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor(sender.getThemeType().getColor(2) + "Click to go to the next page!"))));
            msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, sender.getPrefixCMD() + "help " + (i + 1)));
        }

        sender.getSession().sendPacket(new ServerChatPacket(msg, new TextComponent(" "), msg2));
    }
}