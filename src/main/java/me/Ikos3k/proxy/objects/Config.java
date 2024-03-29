package me.Ikos3k.proxy.objects;

import me.Ikos3k.proxy.protocol.data.DebugType;
import me.Ikos3k.proxy.utils.json.JsonInfo;

import java.util.Arrays;
import java.util.List;

public class Config {
    @JsonInfo()
    public int port = 1337;

    @JsonInfo(object = "motd")
    public String versionInfo = "&4BetterProxy";
    public String line1 = "     &f✖ &l&m\\-\\-\\-\\-&r&f&l>> &6BetterProxy &f&l<<&m-/-/-/-/&r&f ✖&r";
    public String line2 = "           &c----&e/ &6✯ &eProxy by Ikos3k &6✯ &e\\&c----";
    public List<String> playerList = Arrays.asList(
            "&f-------------------------------------------------------",
            "                     &8Supported versions:",
            " &e%supported_versions%",
            "&f-------------------------------------------------------");

    public String icon = "icon.png";
    public int protocol = 0;

    @JsonInfo(object = "debug")
    public DebugType debugType = DebugType.LEGIBLE;
    public boolean showCustomPackets = true;
    public boolean debugger = true;

    @JsonInfo()
    public String proxyVersion = "3.2";
}