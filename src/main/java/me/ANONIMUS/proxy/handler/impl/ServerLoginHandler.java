package me.ANONIMUS.proxy.handler.impl;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.handler.ServerHandler;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.managers.SkinManager;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginDisconnectPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.proxy.utils.*;
import me.kbrewster.mojangapi.MojangAPI;

import java.util.Objects;
import java.util.UUID;

public class ServerLoginHandler extends ServerHandler {
    public ServerLoginHandler(Player player) { super(player); }

    @Override
    public void disconnected() {
        if(player != null && player.getAccount() != null) {
            System.out.println("[" + player.getAccount().getUsername() + "] Disconnected during login sequence!");
        }
    }

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientLoginStartPacket) {
            final String playerName = ((ClientLoginStartPacket) packet).getUsername();
            if(PlayerManager.getPlayers().size() > 1) {
                for (Player p : PlayerManager.getPlayers()) {
                    if (p.getAccount() != null && p.getAccount().getUsername().equals(playerName)) {
                        player.getSession().sendPacket(new ServerLoginDisconnectPacket(ChatUtil.fixColor("&4The player with this nickname is already on the proxy!")));
                        return;
                    }
                }
            }
            for (Account account : BetterProxy.getInstance().getAccounts()) {
                if (account.getUsername().equals(playerName)) {
                    player.getSession().sendPacket(new ServerLoginSetCompressionPacket(256));
                    player.getSession().setCompressionThreshold(256);
                    player.getSession().sendPacket(new ServerLoginSuccessPacket(UUID.randomUUID(), playerName));
                    player.getSession().setConnectionState(ConnectionState.PLAY);
                    player.getSession().setPacketHandler(new ServerPlayHandler(player));

                    try {
                        new SkinManager(new GameProfile(MojangAPI.getUUID(playerName), playerName), player);
                    } catch (Exception ignored) { }

                    player.setAccount(account);
                    WorldUtil.emptyWorld(player);
                    System.out.println("[" + account.getUsername() + "] Connected!");

                    ChatUtil.clearChat(100, player);
                    ScoreboardUtil.sendScoreboard(player);
                    if(CalendarUtil.isHoliday()) {
                        PacketUtil.sendTitle(player, ";D", player.getThemeType().getColor(1) + Objects.requireNonNull(CalendarUtil.getHoliday()).getWishes() + "!");
                    }
                    ChatUtil.sendBroadcastMessage(player.getThemeType().getColor(1) + ">> &8Player " + player.getThemeType().getColor(1) + playerName + " &8has connected to the " + player.getThemeType().getColor(1) + "BetterProxy &8(" + player.getThemeType().getColor(2) + ProtocolType.getByProtocolID(player.getSession().getProtocolID()).getPrefix() + "&8)", false);
                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Welcome to " + player.getThemeType().getColor(1) + "BetterProxy &8by &4ANONIMUS", player, false);
                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Supported versions: *1.8.X&8, *1.9.2&8, *1.9.3&8, *1.9.4&8, *1.10.X&8, *1.12.2".replace("*", player.getThemeType().getColor(2)), player, false);
                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Log in using the command: " + player.getThemeType().getColor(1) + player.getPrefixCMD() + "login [haslo]", player, false);
                    PacketUtil.sendBoosBar(player, ChatUtil.fixColor("&fWelcome to " + player.getThemeType().getColor(1) + "BetterProxy &fby &4ANONIMUS"));
                    return;
                }
            }
            player.getSession().sendPacket(new ServerLoginDisconnectPacket(ChatUtil.fixColor("&4You don't have access...")));
        }
    }
}