package me.ANONIMUS.proxy.handler.impl;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.handler.ServerHandler;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.status.PlayerInfo;
import me.ANONIMUS.proxy.protocol.data.status.ServerStatusInfo;
import me.ANONIMUS.proxy.protocol.data.status.VersionInfo;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.client.status.ClientStatusPingPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.status.ClientStatusRequestPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.status.ServerStatusPongPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.status.ServerStatusResponsePacket;
import me.ANONIMUS.proxy.utils.ChatUtil;
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
    public void disconnect() { }

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientStatusRequestPacket) {
            System.out.println("> Ping packet received from: " + player.getSession().getChannel().localAddress());

            final VersionInfo versionInfo = new VersionInfo(ChatUtil.fixColor(BetterProxy.getInstance().getConfigManager().getConfig().versionInfo), BetterProxy.getInstance().getConfigManager().getConfig().protocol);
            int i = 0;
            GameProfile[] gp = new GameProfile[BetterProxy.getInstance().getConfigManager().getConfig().playerList.size()];
            for (String s : BetterProxy.getInstance().getConfigManager().getConfig().playerList) {
                s = s.replace("%supported_versions%", "&e" + Arrays.stream(ProtocolType.values()).filter(protocolType -> protocolType != ProtocolType.PROTOCOL_UNKNOWN).map(ProtocolType::getPrefix).collect(Collectors.joining(ChatUtil.fixColor("&8, &e"))));
                gp[i] = new GameProfile(ChatUtil.fixColor(s), UUID.randomUUID());
                ++i;
            }
            final PlayerInfo playerInfo = new PlayerInfo(0, 0, gp);
            final BaseComponent[] desc = new ComponentBuilder(ChatUtil.fixColor(BetterProxy.getInstance().getConfigManager().getConfig().line1 + "&r\n" + BetterProxy.getInstance().getConfigManager().getConfig().line2)).create();

            player.getSession().sendPacket(new ServerStatusResponsePacket(new ServerStatusInfo(versionInfo, playerInfo, desc, BetterProxy.getInstance().getServer().getIcon())));
            player.getSession().sendPacket(new ServerStatusPongPacket(0));

            player.getSession().getChannel().close();
        } else if (packet instanceof ClientStatusPingPacket) {
            player.getSession().sendPacket(new ServerStatusPongPacket(((ClientStatusPingPacket) packet).getTime()));
            player.getSession().getChannel().close();
        }
    }
}