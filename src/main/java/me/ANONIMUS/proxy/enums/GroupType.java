package me.ANONIMUS.proxy.enums;

import java.util.Arrays;

public enum GroupType {
    ROOT("&4ROOT", 3, 0),
    ADMIN("&cADMIN", 2, 1),
    VIP("&6VIP", 1, 3),
    USER("&7USER", 0, 5);

    private final String prefix;
    private final int permission;
    private final int delayCMD;

    GroupType(final String prefix, final int permission, final int delayCMD) {
        this.prefix = prefix;
        this.permission = permission;
        this.delayCMD = delayCMD;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPermissionLevel() {
        return permission;
    }

    public int getDelayCMD() {
        return delayCMD;
    }

    public static GroupType getByPermission(int permission) {
        return Arrays.stream(values()).filter(gp -> gp.permission == permission).findFirst().orElse(GroupType.USER);
    }
}