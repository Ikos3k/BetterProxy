package me.Ikos3k.proxy.protocol.data.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PositionType {
    LIST(0), SIDEBAR(1), BELOWNAME(2);

    private final int id;

    public static PositionType getById(int id) {
        return Arrays.stream(values()).filter(v -> v.id == id).findFirst().orElse(LIST);
    }
}
