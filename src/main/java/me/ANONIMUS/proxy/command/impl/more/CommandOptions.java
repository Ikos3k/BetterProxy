package me.ANONIMUS.proxy.command.impl.more;

import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.protocol.data.ItemStack;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerOpenWindowPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerWindowItemsPacket;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.utils.proxy.ItemUtil;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Option;
import me.ANONIMUS.proxy.protocol.data.WindowType;

import java.util.ArrayList;
import java.util.List;

public class CommandOptions extends Command {
    public CommandOptions() {
        super("options", "settings", "options :d", "[list, set] [option] [value]", CommandType.MORE, ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if (args[1].equals("list")) {
            List<ItemStack> items = new ArrayList<>();
            sender.getOptionsManager().getOptions().forEach(option -> items.add(ItemUtil.option(option)));
            sender.getSession().sendPacket(new ServerOpenWindowPacket(234, WindowType.CHEST, "SETTINGS", 9));
            sender.getSession().sendPacket(new ServerWindowItemsPacket(234, items));
            return;
        }
        if (args[1].equals("set")) {
            Option option = sender.getOptionsManager().getOptionByName(args[2]);
            if(option == null) {
                ChatUtil.sendChatMessage("&cThe specified option was not found!", sender, true);
                return;
            }
            option.setEnabled(sender, Boolean.parseBoolean(args[3]));
        }
    }
}