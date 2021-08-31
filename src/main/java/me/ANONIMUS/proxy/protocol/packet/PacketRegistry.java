package me.ANONIMUS.proxy.protocol.packet;

import lombok.SneakyThrows;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.proxy.utils.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class PacketRegistry {
    @SneakyThrows
    public void init() {
        Arrays.asList(PacketDirection.values()).forEach(direction -> Arrays.stream(ConnectionState.values()).filter(connectionState -> connectionState != ConnectionState.HANDSHAKE).forEach(state -> ReflectionUtil.getClasses("me.ANONIMUS.proxy.protocol.packet.impl." + direction.packetsPackageName + "." + state.name().toLowerCase(), Packet.class).forEach(p -> {
            try {
                Packet packet = p.newInstance();
                if (!Modifier.isPublic(packet.getClass().getModifiers())) {
                    throw new IllegalAccessException("Packet " + packet.getClass().getSimpleName() + " has a non public default constructor.");
                }

                state.getPacketsByDirection(direction).add(packet);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        })));
    }

    public Packet createPacket(ConnectionState connectionState, PacketDirection direction, int id, int protocol) {
        Packet packetIn = getPacket(connectionState, direction, id, protocol);

        if (packetIn == null) return new CustomPacket(id);

        try {
            Constructor<? extends Packet> constructor = packetIn.getClass().getDeclaredConstructor();

            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + packetIn.getClass().getName() + "\".", e);
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