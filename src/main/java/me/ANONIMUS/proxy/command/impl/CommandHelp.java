package me.ANONIMUS.proxy.command.impl;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerChatPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help", null, "helpful commands", "[page]", null, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        List<Command> list = new ArrayList<>();
        BetterProxy.getInstance().getCommandManager().getCommands().stream().filter(command -> command.getCommandType() != null).forEach(list::add);
        list.sort(Comparator.comparingInt(s -> ChatUtil.getStringWidth(s.getPrefix())));


        int i = 1;
        if(args.length == 2) {
            i = Integer.parseInt(args[1]);
        }

        int k = i - 1;
        int l = Math.min((k + 1) * 10, list.size());
        int j = (list.size() - 1) / 10;

        if(!(i > 0)) {
            ChatUtil.sendChatMessage ("&cThe given number (" + i + ") is too small, must be at least 1", sender, false);
            return;
        }

        if(i > (j + 1)) {
            ChatUtil.sendChatMessage ("&cThe given number (" + (k + 1) + ") is too high, cannot exceed" + (j + 1), sender, false);
            return;
        }

        ChatUtil.clearChat(1, sender);
        ChatUtil.sendChatMessage (" &8 --- Showing help page: &6" + (k + 1) + " &8of &6" + (j + 1) + " &8---", sender, false);
        for (int i1 = k * 10; i1 < l; ++i1) {
            final Command command = list.get(i1);
            ChatUtil.sendHoverMessage(sender, "&8>> &f" + sender.getPrefixCMD() + command.getPrefix() + " &7" + command.getUsage(), "&6" + command.getDesc());
        }

        TextComponent msg = new TextComponent(ChatUtil.fixColor((i != 1) ? "&c[PREVIOUS]": ""));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor("&eClick to go to the previous page!"))));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, sender.getPrefixCMD() + "help " + (i - 1)));

        TextComponent msg1 = new TextComponent(ChatUtil.fixColor((k + 1) != (j + 1) ? "&a[NEXT]" : ""));
        msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor("&eClick to go to the next page!"))));
        msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, sender.getPrefixCMD() + "help " + (i + 1)));

        sender.getSession().sendPacket(new ServerChatPacket(msg, new TextComponent(" "), msg1));
    }
}