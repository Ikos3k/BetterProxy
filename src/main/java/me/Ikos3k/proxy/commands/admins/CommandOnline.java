package me.Ikos3k.proxy.commands.admins;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.ProtocolType;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandOnline extends Command {
    public CommandOnline() {
        super("online", "users", null, null, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        BetterProxy.getInstance().getPlayerManager().elements.forEach(player -> {
            TextComponent actions = new TextComponent(ChatUtil.fixColor("\n&7-----------------\n&dACTIONS:\n"));
                actions.addExtra(new TextComponent(ChatUtil.fixColor("&ckick"))
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor("&4click to kick player!"))))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, sender.getPrefixCMD() + "kick " + player.getUsername() + " bye")));

                ChatUtil.sendChatMessage(new BaseComponent[] {
                        new TextComponent(ChatUtil.fixColor("&8(&f" + ProtocolType.getByProtocolID(player.getSession().getProtocolID()).getPrefix() + "&8) &8[" + player.getAccount().getGroup().getPrefix() + "&8] " + player.getThemeType().getColor(1) + player.getUsername()))
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor("server: " + (!player.isConnected() ? "&4not connected" : player.getServerData().getHost()))))),
                        actions
                }, sender, false);
        });
    }
}