package me.ANONIMUS.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum WindowAction {
    CLICK_ITEM(0),
    SHIFT_CLICK_ITEM(1),
    MOVE_TO_HOTBAR_SLOT(2),
    CREATIVE_GRAB_MAX_STACK(3),
    DROP_ITEM(4),
    SPREAD_ITEM(5),
    FILL_STACK(6);

    private final int id;

    public static WindowAction getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(null);
    }
}

