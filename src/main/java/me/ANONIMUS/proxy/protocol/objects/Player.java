package me.ANONIMUS.proxy.protocol.objects;

import lombok.Data;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.ThemeType;
import me.ANONIMUS.proxy.enums.TimeType;
import me.ANONIMUS.proxy.handler.impl.ServerLoginHandler;
import me.ANONIMUS.proxy.handler.impl.ServerStatusHandler;
import me.ANONIMUS.proxy.managers.OptionsManager;
import me.ANONIMUS.proxy.managers.SkinManager;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.objects.LastPacket;
import me.ANONIMUS.proxy.objects.ServerData;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerListHeaderFooter;
import me.ANONIMUS.proxy.utils.ChatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Player {
    private final Session session;
    private OptionsManager optionsManager = new OptionsManager();
    private List<PlayerListEntry> tabList = new ArrayList<>();
    private List<Packet> listenedChunks = new ArrayList<>();
    private List<String> players = new ArrayList<>();
    private LastPacket lastPacket = new LastPacket();
    private ThemeType themeType = ThemeType.DEFAULT;
    private TimeType timeType = TimeType.DEFAULT;
    private List<Bot> bots = new ArrayList<>();
    private boolean listenChunks = false;
    private Session remoteSession;
    private String prefixCMD = "#";
    private ServerData serverData;
    private boolean pluginsState;
    private boolean playersState;
    private boolean skin;
    private int motherDelay = 25;
    private String value;
    private String signature;
    private UUID UUID;
    private boolean connected;
    private Account account;
    private boolean mother;
    private boolean logged;

    public void packetReceived(final Packet packet) {
        if (packet instanceof HandshakePacket) {
            final HandshakePacket handshake = (HandshakePacket) packet;
            session.setProtocolID(handshake.getProtocolId());
            switch (handshake.getNextState()) {
                case 1:
                    session.setConnectionState(ConnectionState.STATUS);
                    session.setPacketHandler(new ServerStatusHandler(this));
                    break;
                case 2:
                    session.setConnectionState(ConnectionState.LOGIN);
                    session.setPacketHandler(new ServerLoginHandler(this));
                    break;
            }
            if (session.getConnectionState() == ConnectionState.HANDSHAKE) {
                session.getChannel().close();
            }
        } else {
            session.getPacketHandler().handlePacket(packet);
        }
    }

    public void disconnected() {
        connected = false;
        if (remoteSession != null) {
            remoteSession.getChannel().close();
        }
        if (session != null) {
            if (session.getPacketHandler() != null) {
                session.getPacketHandler().disconnected();
            }
        }
        BetterProxy.getInstance().getPlayerManager().removePlayer(this);
    }

    public void addSkin() {
        new SkinManager(new GameProfile(UUID, account.getUsername()), this);
        if (!isSkin()) {
            PlayerListEntry entry = new PlayerListEntry(new GameProfile(UUID, account.getUsername()));
            tabList.add(entry);
            setSkin(true);
        }
    }

    public boolean isConnected() {
        return remoteSession != null && connected;
    }

    public void updateTab() {
        if (!isConnected()) {
            session.sendPacket(new ServerPlayerListHeaderFooter(ChatUtil.fixColor(
                    "\n&l" + themeType.getColor(1) + "BetterProxy" +
                            "\n" +
                            "\n" +
                            "&7Group: " + account.getGroup().getPrefix() +
                            "\n" +
                            "&7Username: " + themeType.getColor(2) + account.getUsername()
            ),
                    ChatUtil.fixColor(
                            "&7Session: " + themeType.getColor(2) + "Not connected\n\n" +
                                    "&l" + themeType.getColor(1) + "BetterProxy\n"
                    )));
        } else {
            session.sendPacket(new ServerPlayerListHeaderFooter(ChatUtil.fixColor(
                    "\n&l" + themeType.getColor(1) + "BetterProxy" +
                            "\n" +
                            "\n" +
                            "&7Group: " + account.getGroup().getPrefix() +
                            "\n" +
                            "&7Username: " + themeType.getColor(2) + account.getUsername() +
                            "\n" +
                            "\n" +
                            "&7Last received packet" +
                            "\n" +
                            themeType.getColor(2) + lastPacket.getPacket().getClass().getSimpleName() + themeType.getColor(3) + " (" + themeType.getColor(1) + lastPacket.getLastMs() + themeType.getColor(3) + ")\n"
            ),
                    ChatUtil.fixColor(
                            "\n&7Session: " + themeType.getColor(2) + serverData.getHost() +
                                    "\n\n&l" + themeType.getColor(1) + "BetterProxy\n"
                    )));
        }
    }
}