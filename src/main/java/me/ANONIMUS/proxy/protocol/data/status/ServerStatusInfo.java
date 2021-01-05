package me.ANONIMUS.proxy.protocol.data.status;

import lombok.Data;
import net.kyori.adventure.text.Component;

@Data
public class ServerStatusInfo {
    private VersionInfo versionInfo;
    private PlayerInfo playerInfo;
    private Component description;
    private String icon;

    public ServerStatusInfo(final VersionInfo versionInfo, final PlayerInfo playerInfo, final Component description, final String icon) {
        this.versionInfo = versionInfo;
        this.playerInfo = playerInfo;
        this.description = description;
        this.icon = icon;
    }
}