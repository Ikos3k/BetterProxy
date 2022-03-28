package me.Ikos3k.proxy.utils.inventory;

import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerCloseWindowPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerOpenWindowPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerWindowItemsPacket;

public class InventoryUtil {
    public static void openInventory(Inventory inventory, Player player) {
        if(player.getCurrentInventory() != null) {
            closeInventory(player);
        }

        player.getSession().sendPacket(new ServerOpenWindowPacket(inventory.getWindowID(), inventory.getType(), inventory.getName(), inventory.getSlots()));
        player.getSession().sendPacket(new ServerWindowItemsPacket(inventory.getWindowID(), inventory.getItems()));

        inventory.onOpen();
        player.setCurrentInventory(inventory);
    }

    public static void closeInventory(Player player) {
        player.getSession().sendPacket(new ServerCloseWindowPacket(player.getCurrentInventory().getWindowID()));
        player.setCurrentInventory(null);
    }

    public static void reopenInventory(Player player) {
        openInventory(player.getCurrentInventory(), player);
    }
}