package me.ANONIMUS.proxy.protocol.data;

import java.util.Arrays;

public enum WindowType {
    GENERIC_INVENTORY("minecraft:container"),
    ANVIL("minecraft:anvil"),
    BEACON("minecraft:beacon"),
    BREWING_STAND("minecraft:brewing_stand"),
    CHEST("minecraft:chest"),
    CRAFTING_TABLE("minecraft:crafting_table"),
    DISPENSER("minecraft:dispenser"),
    DROPPER("minecraft:dropper"),
    ENCHANTING_TABLE("minecraft:enchanting_table"),
    FURNACE("minecraft:furnace"),
    HOPPER("minecraft:hopper"),
    VILLAGER("minecraft:villager"),
    HORSE("EntityHorse");

    private final String id;

    WindowType(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public static WindowType getById(String id) {
        return Arrays.stream(values()).filter((gm) -> gm.id.equals(id)).findFirst().orElse(GENERIC_INVENTORY);
    }
}
