package me.Ikos3k.proxy.utils.inventory;

import lombok.Data;
import me.Ikos3k.proxy.protocol.data.ItemStack;
import me.Ikos3k.proxy.protocol.data.WindowAction;
import me.Ikos3k.proxy.protocol.data.WindowType;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Inventory {
    public List<ItemStack> items = new ArrayList<>();
    public final int windowID;
    public final WindowType type;
    public final String name;
    public final boolean isClosable;

    public Inventory(String name) {
        this(name, false);
    }

    public Inventory(String name, boolean isClosable) {
        this.type = WindowType.CHEST;
        this.windowID = 1;
        this.name = name;
        this.isClosable = isClosable;

        init();
    }

    public void init() { }

    public void onAction(WindowAction action, ItemStack itemStack, int slot, int button) { }

    public void onClickAnotherGui(WindowAction action, ItemStack itemStack, int slot, int button) { }

    public void onClickOutsideGui(int button) { }

    public void onOpen() { }

    public void onClose() { }

    public void addItem(ItemStack item) {
        this.items.add(item);
    }

    public void setItem(ItemStack item, int slot) {
        if(slot > items.size() - 1) {
            for (int i = items.size(); i < slot; i++) {
                this.items.add(null);
            }
        }

        this.items.set(slot - 1, item);
    }

    public int getSlots() {
        if (this.items.size() % 9 == 0) {
            return this.items.size();
        } else {
            return (this.items.size() / 9 + 1) * 9;
        }
    }
}