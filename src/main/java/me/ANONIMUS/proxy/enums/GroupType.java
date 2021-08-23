package me.ANONIMUS.proxy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum GroupType {
    ROOT("&4ROOT", 3, 0),
    ADMIN("&cADMIN", 2, 1),
    VIP("&6VIP", 1, 3),
    USER("&7USER", 0, 5);

    private final String prefix;
    private final int permission;
    private final int delayCMD;

    public static GroupType getByPermission(int permission) {
        return Arrays.stream(values()).filter(gp -> gp.permission == permission).findFirst().orElse(GroupType.USER);
    }
}