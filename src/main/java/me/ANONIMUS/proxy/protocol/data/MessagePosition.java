package me.ANONIMUS.proxy.protocol.data;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MessagePosition {
    CHATBOX(0), SYSTEM(1), HOTBAR(2);

    private final int id;

    MessagePosition(int id) {
        this.id = id;
    }

    public static MessagePosition getById(int id) {
        return Arrays.stream(values()).filter(m -> m.id == id).findFirst().orElse(MessagePosition.CHATBOX);
    }
}