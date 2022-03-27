package me.Ikos3k.proxy.protocol.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Dimension {
    NETHER(-1), OVERWORLD(0), END(1);

    private final int id;

    public static Dimension getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(OVERWORLD);
    }
}
