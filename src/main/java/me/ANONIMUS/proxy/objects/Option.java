package me.ANONIMUS.proxy.objects;

import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;

public class Option {
    private Player player;
    private final String name;
    private final String[] description;
    private boolean enabled;

    public Option(String name, String... description) {
        this.name = name;
        this.description = description;
        this.enabled = false;
    }

    public Option(boolean defaultValue, String name, String... description) {
        this.name = name;
        this.description = description;
        this.enabled = defaultValue;
    }

    public void toggle(Player player) {
        this.player = player;
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setEnabled(Player player, boolean enabled) {
        this.player = player;
        if(this.enabled != enabled) {
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public boolean hasDescription() {
        return description.length > 0;
    }

    public String[] getDescription() {
        return description;
    }

    public void onEnable() { ChatUtil.sendChatMessage("&6>> &8Successfully &aenabled &8option &6" + name, player, false); }

    public void onDisable() { ChatUtil.sendChatMessage("&6>> &8Successfully &cdisabled &8option &6" + name, player, false); }
}