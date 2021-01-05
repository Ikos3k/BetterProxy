package me.ANONIMUS.proxy.protocol.data.status;

import lombok.Data;

@Data
public class VersionInfo
{
    private String versionName;
    private int protocolVersion;

    public VersionInfo(final String versionName, final int protocolVersion) {
        this.versionName = versionName;
        this.protocolVersion = protocolVersion;
    }
}