package me.ANONIMUS.proxy.protocol.packet;

import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketRegistry {
    public void init() {
        Arrays.asList(PacketDirection.values()).forEach(direction -> Arrays.stream(ConnectionState.values()).filter(connectionState -> connectionState != ConnectionState.HANDSHAKE).forEach(state -> new Reflections("me.ANONIMUS.proxy.protocol.packet.impl." + direction.packetsPackageName.toLowerCase() + "." + state.name().toLowerCase()).getSubTypesOf(Packet.class).forEach(p -> {
            try {
                final Packet packet = p.newInstance();
                packet.getProtocolList().forEach(protocol -> {
                    if(state.getPacketsByDirection(direction).get(packet) == null) {
                        List<Protocol> protocols = new ArrayList<>();
                        protocols.add(protocol);
                        state.getPacketsByDirection(direction).put(packet, protocols);
                    } else {
                        state.getPacketsByDirection(direction).get(packet).add(protocol);
                    }
                });
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        })));
    }

    public Packet createPacket(ConnectionState connectionState, PacketDirection direction, int id, int protocol){
        Packet packetIn = getPacket(connectionState,direction,new Protocol(id,protocol));
        if(packetIn == null) return new CustomPacket(id);
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
        } catch(Exception e) {
            throw new IllegalStateException("Failed to instantiate packet \"" + packet.getName() + "\".", e);
        }
    }

    private Packet getPacket(ConnectionState connectionState, PacketDirection direction, Protocol protocol) {
        if(connectionState == ConnectionState.HANDSHAKE) {
            return new HandshakePacket();
        }

        for(Packet packet : connectionState.getPacketsByDirection(direction).keySet()) {
            for(Protocol protocol1 : connectionState.getPacketsByDirection(direction).get(packet)) {
                if(protocol1.equals(protocol)) {
                    return packet;
                }
            }
        }

        return null;
    }
}