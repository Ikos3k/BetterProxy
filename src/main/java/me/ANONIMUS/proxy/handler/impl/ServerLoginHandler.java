package me.ANONIMUS.proxy.handler.impl;

import lombok.SneakyThrows;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.handler.ServerHandler;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginDisconnectPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.proxy.utils.*;
import me.kbrewster.exceptions.InvalidPlayerException;
import me.kbrewster.mojangapi.MojangAPI;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServerLoginHandler extends ServerHandler {
    public ServerLoginHandler(Player player) {
        super(player);
    }

    @Override
    public void disconnect() {
        System.out.println("[" + player.getUsername() + "] Disconnected during login sequence!");
    }

    @SneakyThrows
    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof ClientLoginStartPacket) {
            final String playerName = ((ClientLoginStartPacket) packet).getUsername();
            player.getSession().setUsername(playerName);
            if (BetterProxy.getInstance().getPlayerManager().getPlayers().size() > 1) {
                for (Player p : BetterProxy.getInstance().getPlayerManager().getPlayers()) {
                    if (p.getAccount() != null && p.getUsername().equals(playerName)) {
                        player.getSession().sendPacket(new ServerLoginDisconnectPacket(ChatUtil.fixColor("&4The player with this nickname is already on the proxy!")));
                        return;
                    }
                }
            }

            for(Map.Entry<String, Account> account : BetterProxy.getInstance().getAccounts().entrySet()) {
                if (account.getKey().equals(playerName)) {
                    UUID uuid;
                    try {
                        uuid = MojangAPI.getUUID(playerName);
                    } catch (InvalidPlayerException ignored) {
                        uuid = UUID.randomUUID();
                    }

                    player.getSession().sendPacket(new ServerLoginSetCompressionPacket(256));
                    player.getSession().setCompressionThreshold(256);
                    player.getSession().sendPacket(new ServerLoginSuccessPacket(uuid, playerName));
                    player.getSession().setConnectionState(ConnectionState.PLAY);
                    player.getSession().setPacketHandler(new ServerPlayHandler(player));
                    player.setAccount(account.getValue());
                    player.loadOptions();

                    SkinUtil.showSkin(player.getSession(), uuid, player.getSkin());

                    WorldUtil.emptyWorld(player);
                    System.out.println("[" + playerName + "] Connected!");
                    ChatUtil.clearChat(100, player);

                    ScoreboardUtil.sendScoreboard(player);

                    if (CalendarUtil.isHoliday()) {
                        PacketUtil.sendTitle(player, ";D", player.getThemeType().getColor(1) + Objects.requireNonNull(CalendarUtil.getHoliday()).getWishes() + "!");
                    }

                    ChatUtil.sendBroadcastMessage(player.getThemeType().getColor(1) + ">> &8Player " + player.getThemeType().getColor(1) + playerName + " &8has connected to the " + player.getThemeType().getColor(1) + "BetterProxy &8(" + player.getThemeType().getColor(2) + ProtocolType.getByProtocolID(player.getSession().getProtocolID()).getPrefix() + "&8)", false);

                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Welcome to " + player.getThemeType().getColor(1) + "BetterProxy &8by &4ANONIMUS", player, false);
                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Supported versions: &e" + Arrays.stream(ProtocolType.values()).filter(protocolType ->
                            protocolType != ProtocolType.PROTOCOL_UNKNOWN)
                            .map(ProtocolType::getPrefix).collect(Collectors.joining(ChatUtil.fixColor("&8, &e"))), player, false);
                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Log in using the command: " + player.getThemeType().getColor(1) + player.getPrefixCMD() + "login [password]", player, false);

                    PacketUtil.sendBoosBar(player, ChatUtil.fixColor("&fWelcome to " + player.getThemeType().getColor(1) + "BetterProxy &fby &4ANONIMUS"));
                    return;
                }
            }
            player.getSession().sendPacket(new ServerLoginDisconnectPacket(ChatUtil.fixColor("&4You don't have access...")));
        }
    }
}