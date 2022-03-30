package me.Ikos3k.proxy.handler.impl;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.handler.ServerHandler;
import me.Ikos3k.proxy.objects.Config;
import me.Ikos3k.proxy.protocol.ProtocolType;
import me.Ikos3k.proxy.protocol.data.status.PlayerInfo;
import me.Ikos3k.proxy.protocol.data.status.ServerStatusInfo;
import me.Ikos3k.proxy.protocol.data.status.VersionInfo;
import me.Ikos3k.proxy.protocol.objects.GameProfile;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.impl.client.status.ClientStatusPingPacket;
import me.Ikos3k.proxy.protocol.packet.impl.client.status.ClientStatusRequestPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.status.ServerStatusPongPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.status.ServerStatusResponsePacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServerStatusHandler extends ServerHandler {
    public ServerStatusHandler(Player player) {
        super(player);
    }

    @Override
    public void disconnect() {}

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientStatusRequestPacket) {
//            System.out.println("> Ping packet received from: " + player.getSession().getChannel().localAddress());
            Config config = BetterProxy.getInstance().getConfigManager().getConfig();
            String protocols = Arrays.stream(ProtocolType.values()).filter(protocolType -> protocolType != ProtocolType.PROTOCOL_UNKNOWN).map(ProtocolType::getPrefix).collect(Collectors.joining(ChatUtil.fixColor("&8, &e")));
            VersionInfo versionInfo = new VersionInfo(ChatUtil.fixColor(config.versionInfo + " &7(&8" + config.proxyVersion + "&7)"), config.protocol);
            int i = 0;
            GameProfile[] gp = new GameProfile[config.playerList.size()];
            for (String s : BetterProxy.getInstance().getConfigManager().getConfig().playerList) {
                s = s.replace("%supported_versions%", "&e" + protocols);
                s = s.replace("%online_players%", "&e" + BetterProxy.getInstance().getPlayerManager().elements.size());
                s = s.replace("%logged_players%", "&e" + BetterProxy.getInstance().getPlayerManager().elements.stream().filter(Player::isLogged).count());
                gp[i] = new GameProfile(ChatUtil.fixColor(s), UUID.randomUUID());
                ++i;
            }
            PlayerInfo playerInfo = new PlayerInfo(0, 0, gp);
            BaseComponent[] desc = new ComponentBuilder(ChatUtil.fixColor(config.line1 + "&r\n" + config.line2)).create();

            player.getSession().sendPacket(new ServerStatusResponsePacket(new ServerStatusInfo(versionInfo, playerInfo, desc, BetterProxy.getInstance().getServer().getIcon())));
            player.getSession().sendPacket(new ServerStatusPongPacket(0));

            player.getSession().getChannel().close();
        } else if (packet instanceof ClientStatusPingPacket) {
            player.getSession().sendPacket(new ServerStatusPongPacket(((ClientStatusPingPacket) packet).getTime()));
            player.getSession().getChannel().close();
        }
    }
}