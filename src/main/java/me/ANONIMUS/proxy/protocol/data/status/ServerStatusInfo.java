package me.ANONIMUS.proxy.protocol.data.status;

import lombok.Data;
import net.md_5.bungee.api.chat.BaseComponent;

@Data
public class ServerStatusInfo {
    private VersionInfo versionInfo;
    private PlayerInfo playerInfo;
    private BaseComponent[] description;
    private String icon;

    public ServerStatusInfo(final VersionInfo versionInfo, final PlayerInfo playerInfo, final BaseComponent[] description, final String icon) {
        this.versionInfo = versionInfo;
        this.playerInfo = playerInfo;
        this.description = description;
        this.icon = icon;
    }
}