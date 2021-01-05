package me.ANONIMUS.proxy.protocol.objects;

import lombok.Data;
import me.ANONIMUS.proxy.enums.TimeType;
import me.ANONIMUS.proxy.handler.impl.ServerLoginHandler;
import me.ANONIMUS.proxy.handler.impl.ServerStatusHandler;
import me.ANONIMUS.proxy.managers.OptionsManager;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.objects.LastPacket;
import me.ANONIMUS.proxy.objects.ServerData;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player {
    private final Session session;
    private TimeType timeType = TimeType.DEFAULT;
    private Session remoteSession;
    private Account account;
    private boolean mother;
    private boolean pluginsState;
    private boolean playersState;
    private boolean connected;
    private boolean logged;
    private LastPacket lastPacket = new LastPacket();
    private OptionsManager optionsManager = new OptionsManager();
    private ServerData serverData;
    private String prefixCMD = "#";
    private List<String> players = new ArrayList<>();
    private List<PlayerListEntry> tabList = new ArrayList<>();
    private List<Bot> bots = new ArrayList<>();

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
        if(session != null) {
            session.getChannel().close();
            if (session.getPacketHandler() != null) {
                session.getPacketHandler().disconnected();
            }
        }
        PlayerManager.removePlayer(this);
    }

    public boolean isConnected() {
        return remoteSession != null && connected;
    }
}