package me.ANONIMUS.proxy.protocol.packet;

import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class PacketRegistry {
    public void init() {
        Arrays.asList(PacketDirection.values()).forEach(direction -> Arrays.stream(ConnectionState.values()).filter(connectionState -> connectionState != ConnectionState.HANDSHAKE).forEach(state -> new Reflections("me.ANONIMUS.proxy.protocol.packet.impl." + direction.packetsPackageName.toLowerCase() + "." + state.name().toLowerCase()).getSubTypesOf(Packet.class).forEach(p -> {
            try {
                state.getPacketsByDirection(direction).add(p.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        })));
    }

    public Packet createPacket(ConnectionState connectionState, PacketDirection direction, int id, int protocol) {
        Packet packetIn = getPacket(connectionState, direction, id, protocol);
        if (packetIn == null) return new CustomPacket(id);
        Class<? extends Packet> packet = packetIn.getClass();
        try {
            Constructor<? extends Packet> constructor = packet.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                throw new IllegalAccessException("Packet " + packet.getName() + " has a non public default constructor.");
            }

            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + packet.getName() + "\".", e);
        }
    }

    private Packet getPacket(ConnectionState connectionState, PacketDirection direction, int id, int protocol) {
        if (connectionState == ConnectionState.HANDSHAKE) {
            return new HandshakePacket();
        }

        for (Packet packet : connectionState.getPacketsByDirection(direction)) {
            for (Protocol protocol2 : packet.getProtocolList()) {
                if (protocol2.getId() == id) {
                    for (int p : protocol2.getProtocols()) {
                        if (p == protocol) {
                            return packet;
                        }
                    }
                }
            }
        }

        return null;
    }
}