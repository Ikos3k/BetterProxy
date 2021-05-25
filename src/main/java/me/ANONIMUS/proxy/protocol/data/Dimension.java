package me.ANONIMUS.proxy.protocol.data;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Dimension {
    NETHER(-1), OVERWORLD(0), END(1);

    private final int id;

    Dimension(int id) {
        this.id = id;
    }

    public static Dimension getById(int id) {
        return Arrays.stream(values()).filter(gm -> gm.id == id).findFirst().orElse(Dimension.OVERWORLD);
    }
}
