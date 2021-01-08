package me.ANONIMUS.proxy.handler.impl;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.handler.ServerHandler;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class ServerStatusHandler extends ServerHandler {
    public ServerStatusHandler(Player player) {
        super(player);
    }

    @Override
    public void disconnected() { }

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientStatusRequestPacket) {
            try {
                final File statusFile = new File(BetterProxy.getInstance().getDirFolder() + "/" + BetterProxy.getInstance().getConfigManager().getConfig().icon);
                BufferedImage bufferedImage = null;
                if (statusFile.exists()) {
                    try {
                        bufferedImage = ImageIO.read(new File(BetterProxy.getInstance().getDirFolder() + "/" + BetterProxy.getInstance().getConfigManager().getConfig().icon));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final VersionInfo versionInfo = new VersionInfo(ChatUtil.fixColor(BetterProxy.getInstance().getConfigManager().getConfig().versionInfo), BetterProxy.getInstance().getConfigManager().getConfig().protocol);
                int i = 0;
                GameProfile[] gp = new GameProfile[BetterProxy.getInstance().getConfigManager().getConfig().playerList.size()];
                for (String s : BetterProxy.getInstance().getConfigManager().getConfig().playerList) {
                    gp[i] = new GameProfile(ChatUtil.fixColor(s), UUID.randomUUID());
                    ++i;
                }
                final PlayerInfo playerInfo = new PlayerInfo(0, 0, gp);
                final BaseComponent[] desc = new ComponentBuilder(ChatUtil.fixColor(BetterProxy.getInstance().getConfigManager().getConfig().line1 + "&r\n" + BetterProxy.getInstance().getConfigManager().getConfig().line2)).create();
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                assert bufferedImage != null;
                ImageIO.write(bufferedImage, "png", Base64.getEncoder().wrap(os));

                player.getSession().sendPacket(new ServerStatusResponsePacket(new ServerStatusInfo(versionInfo, playerInfo, desc, statusFile.exists() ? "data:image/png;base64," + os.toString(StandardCharsets.ISO_8859_1.name()) : null)));
            } catch (Exception ignored) {}
            player.getSession().sendPacket(new ServerStatusPongPacket(0));
            player.getSession().getChannel().close();
        } else if (packet instanceof ClientStatusPingPacket) {
            player.getSession().sendPacket(new ServerStatusPongPacket(((ClientStatusPingPacket) packet).getTime()));
            player.getSession().getChannel().close();
        }
    }
}