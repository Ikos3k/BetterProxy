package me.Ikos3k.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MessagePosition {
    CHATBOX(0), SYSTEM(1), HOTBAR(2);

    private final int id;

    public static MessagePosition getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(CHATBOX);
    }
}