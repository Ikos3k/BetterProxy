package me.ANONIMUS.proxy.protocol.data;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TitleAction {
    TITLE(0), SUBTITLE(1), TIMES(2), HIDE(3), RESET(4);

    private final int id;

    TitleAction(int id) {
        this.id = id;
    }

    public static TitleAction getById(int id) {
        return Arrays.stream(TitleAction.values()).filter(gm -> gm.id == id).findFirst().orElse(TitleAction.RESET);
    }
}
