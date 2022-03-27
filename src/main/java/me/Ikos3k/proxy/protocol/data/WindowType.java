package me.Ikos3k.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
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

    private final String value;

    public static WindowType getById(String value) {
        return Arrays.stream(values()).filter(v -> v.value.equals(value)).findFirst().orElse(GENERIC_INVENTORY);
    }
}
