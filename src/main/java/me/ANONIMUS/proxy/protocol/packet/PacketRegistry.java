package me.ANONIMUS.proxy.protocol.packet;

import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;

public class PacketRegistry {
    private final HashMap<Protocol, Packet> CLIENT_STATUS = new HashMap<>();
    private final HashMap<Protocol, Packet> CLIENT_LOGIN = new HashMap<>();
    private final HashMap<Protocol, Packet> CLIENT_PLAY = new HashMap<>();

    private final HashMap<Protocol, Packet> SERVER_STATUS = new HashMap<>();
    private final HashMap<Protocol, Packet> SERVER_LOGIN = new HashMap<>();
    private final HashMap<Protocol, Packet> SERVER_PLAY = new HashMap<>();

    public void registerPacket(ConnectionState connectionState, PacketDirection direction, Packet packet) {
        for(Protocol protocol : packet.getProtocolList()) {
            switch (direction) {
                case SERVERBOUND:
                    switch (connectionState) {
                        case LOGIN:
                            CLIENT_LOGIN.put(protocol, packet);
                            break;
                        case PLAY:
                            CLIENT_PLAY.put(protocol, packet);
                            break;
                        case STATUS:
                            CLIENT_STATUS.put(protocol, packet);
                            break;
                    }
                    break;
                case CLIENTBOUND:
                    switch (connectionState) {
                        case LOGIN:
                            SERVER_LOGIN.put(protocol, packet);
                            break;
                        case PLAY:
                            SERVER_PLAY.put(protocol, packet);
                            break;
                        case STATUS:
                            SERVER_STATUS.put(protocol, packet);
                            break;
                    }
                    break;
            }
        }
    }

    public void load() {
        Arrays.asList(PacketDirection.values()).forEach(direction -> Arrays.stream(ConnectionState.values()).filter(connectionState -> connectionState != ConnectionState.HANDSHAKE).forEach(state -> {
            new Reflections("me.AlshainTeam.proxy.protocol.packet.impl." + direction.packetsPackageName.toLowerCase() + "." + state.name().toLowerCase()).getSubTypesOf(Packet.class).forEach(p -> {
                try {
                    registerPacket(state, direction, p.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }));
    }

    public Packet createPacket(ConnectionState connectionState, PacketDirection direction, int id, int protocol){
        Packet packetIn = getPacket(connectionState,direction,new Protocol(id,protocol));
        if(packetIn == null) return new CustomPacket(id);
        Class<? extends Packet> packet = packetIn.getClass();
        try {
            Constructor<? extends Packet> constructor = packet.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        } catch(Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + packet.getName() + "\".", e);
        }
    }

    private Packet getPacket(ConnectionState connectionState, PacketDirection direction, Protocol protocol) {
        switch(direction) {
            case SERVERBOUND:
                switch(connectionState) {
                    case HANDSHAKE:
                        return new HandshakePacket();
                    case LOGIN:
                        return CLIENT_LOGIN.get(protocol);
                    case PLAY:
                        return CLIENT_PLAY.get(protocol);
                    case STATUS:
                        return CLIENT_STATUS.get(protocol);
                }
                break;
            case CLIENTBOUND:
                switch(connectionState) {
                    case LOGIN:
                        return SERVER_LOGIN.get(protocol);
                    case PLAY:
                        return SERVER_PLAY.get(protocol);
                    case STATUS:
                        return SERVER_STATUS.get(protocol);

                }
                break;
        }
        return null;
    }
}