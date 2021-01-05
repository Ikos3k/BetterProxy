package me.ANONIMUS.proxy.handler.impl;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.ProtocolType;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginDisconnectPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.proxy.utils.proxy.ScoreboardUtil;
import me.ANONIMUS.proxy.handler.ServerHandler;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.proxy.utils.proxy.ChatUtil;
import me.ANONIMUS.proxy.utils.proxy.WorldUtil;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class ServerLoginHandler extends ServerHandler {
    public ServerLoginHandler(Player player) {
        super(player);
    }

    @Override
    public void disconnected() {
        if(player != null) {
            System.out.println("[" + player.getAccount().getUsername() + "] Disconnected during login sequence!");
        }
    }

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientLoginStartPacket) {
            for (Account account : BetterProxy.getInstance().getAccounts()) {
                if (account.getUsername().equals(((ClientLoginStartPacket) packet).getUsername())) {
                    player.getSession().sendPacket(new ServerLoginSetCompressionPacket(256));
                    player.getSession().setCompressionThreshold(256);
                    player.getSession().sendPacket(new ServerLoginSuccessPacket(UUID.randomUUID(), account.getUsername()));
                    player.getSession().setConnectionState(ConnectionState.PLAY);
                    player.getSession().setPacketHandler(new ServerPlayHandler(player));
                    player.setGameProfile(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + account.getUsername()).getBytes()), account.getUsername()));
                    player.setAccount(account);

                    WorldUtil.emptyWorld(player);
                    System.out.println("[" + account.getUsername() + "] Logged in!");

                    ChatUtil.clearChat(100, player);
                    ScoreboardUtil.sendScoreboard(player);
                    ChatUtil.sendBroadcastMessage("&6>> &8Player &6" + player.getAccount().getUsername() + " &8has connected to the &6BetterProxy &8(&e" + ProtocolType.getByProtocolID(player.getSession().getProtocolID()).getPrefix() + "&8)", false);
                    ChatUtil.sendChatMessage("&6>> &8Welcome to &6BetterProxy &8by &4ANONIMUS", player, false);
                    ChatUtil.sendChatMessage("&6>> &8Supported versions: &e1.8.X&8, &e1.9.3&8, &e1.9.4&8, &e1.12.2", player, false);
                    ChatUtil.sendChatMessage("&6>> &8Log in using the command: &6" + player.getPrefixCMD() + "login [haslo]", player, false);
                    return;
                }
            }
            player.getSession().sendPacket(new ServerLoginDisconnectPacket(Component.text(ChatUtil.fixColor("&4You don't have access..."))));
        }
    }
}