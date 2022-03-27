package me.Ikos3k.proxy.commands.more;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.objects.Option;
import me.Ikos3k.proxy.protocol.data.ItemStack;
import me.Ikos3k.proxy.protocol.data.WindowType;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerOpenWindowPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerWindowItemsPacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandOptions extends Command {
    public CommandOptions() {
        super("options", "settings", "options :d", "[list, set] [option] [value]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        if (args[1].equals("list") && sender.getSession().getProtocolID() == 47) {
            List<ItemStack> items = new ArrayList<>();
            sender.getOptionsManager().elements.forEach(option -> items.add(ItemUtil.option(option)));
            sender.getSession().sendPacket(new ServerOpenWindowPacket(234, WindowType.CHEST, "SETTINGS", 9));
            sender.getSession().sendPacket(new ServerWindowItemsPacket(234, items));
            return;
        }

        if (args[1].equals("set")) {
            Option option = sender.getOptionsManager().getOptionByName(args[2]);

            if (option == null) {
                ChatUtil.sendChatMessage("&cThe specified option was not found!", sender, true);
                return;
            }

            option.setEnabled(Boolean.parseBoolean(args[3]));
        }
    }
}