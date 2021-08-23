package me.ANONIMUS.proxy.objects;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.ChatUtil;

@Data
public class Option {
    private final Player player;
    private final String name;
    private final String[] description;
    private boolean enabled;

    public Option(Player player, String name, String... description) {
        this(player, false, name, description);
    }

    public Option(Player player, boolean defaultValue, String name, String... description) {
        this.player = player;
        this.name = name;
        this.description = description;
        this.enabled = defaultValue;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            toggle();
        } else {
            ChatUtil.sendChatMessage("&6>> &8The &6" + name + " &8option is already " + (enabled ? "&a" : "&c") + enabled, player, false);
        }
    }

    public boolean hasDescription() {
        return description.length > 0;
    }

    public void onEnable() {
        ChatUtil.sendChatMessage("&6>> &8Successfully &aenabled &8option &6" + name, player, false);
    }

    public void onDisable() {
        ChatUtil.sendChatMessage("&6>> &8Successfully &cdisabled &8option &6" + name, player, false);
    }
}