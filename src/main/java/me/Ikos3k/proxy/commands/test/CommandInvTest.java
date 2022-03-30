package me.Ikos3k.proxy.commands.test;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.data.ItemStack;
import me.Ikos3k.proxy.protocol.data.WindowAction;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.inventory.Inventory;
import me.Ikos3k.proxy.utils.inventory.InventoryUtil;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.Collections;

public class CommandInvTest extends Command {
    public CommandInvTest() {
        super("inv", null, null, null, ConnectedType.DISCONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        Inventory inventory = new Inventory(ChatUtil.fixColor("&4E&2XDD&5AAA_TEST_INV")) {
            @Override
            public void init() {
                this.setItems(Arrays.asList(
                    new ItemStack(2).setLoreName(Collections.singletonList(ChatUtil.fixColor("&4click to close gui!"))),
                    new ItemStack(1).setName(ChatUtil.fixColor("&4HI")),
                    null,
                    new ItemStack(2)
                ));
            }

            @Override
            public void onAction(WindowAction action, ItemStack item, int slot, int button) {
                if(slot == 0) {
                    System.out.println("[DEBUG] CLOSE MANUAL!!!!!");
                    InventoryUtil.closeInventory(sender);
                }

                if(action == WindowAction.CLICK_ITEM) {
                    System.out.println("CLICK ON: " + (item != null ? (item.getName() != null ? item.getName() + " (" + ChatColor.stripColor(item.getName()) + ")" : item.getId()) : "null") + " [slot: " + slot + "] [button: " + button + "]");
                }
            }

            @Override
            public void onClickAnotherGui(WindowAction action, ItemStack itemStack, int slot, int button) {
                System.out.println("[DEBUG] ANOTHER GUI");
            }

            @Override
            public void onClickOutsideGui(int button) {
                System.out.println("outside gui!");
            }

            @Override
            public void onClose() {
                System.out.println("[DEBUG] CLOSE AUTOMATIC!");
            }
        };

        InventoryUtil.openInventory(inventory, sender);
    }
}