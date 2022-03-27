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
    }

    public int getSlots() {
        return items.size();
    }

    public void setItem(ItemStack item, int slot) {
        this.items.set(slot, item);
    }

    public void addItem(ItemStack item) {
        this.items.add(item);
    }

    public abstract void onAction(WindowAction action, ItemStack itemStack, int slot, int button);

    public abstract void onClickAnotherGui(WindowAction action, ItemStack itemStack, int slot, int button);

    public abstract void onClickOutsideGui(int button);

    public abstract void onOpen();

    public abstract void onClose();
}