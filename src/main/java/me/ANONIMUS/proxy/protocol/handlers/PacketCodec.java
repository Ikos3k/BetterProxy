package me.ANONIMUS.proxy.protocol.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import lombok.Getter;
import lombok.Setter;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.objects.Config;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.data.DebugType;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.utils.ArrayUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PacketCodec extends ByteToMessageCodec<Packet> {
    private final PacketDirection packetDirection;
    private ConnectionState connectionState;
    private int protocol;

    private final Config config;

    public PacketCodec(ConnectionState connectionState, PacketDirection packetDirection) {
        this.config = BetterProxy.getInstance().getConfigManager().getConfig();

        this.connectionState = connectionState;
        this.packetDirection = packetDirection;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        if (!byteBuf.isWritable()) return;

        final PacketBuffer packetbuffer = new PacketBuffer(byteBuf);

        if(packet instanceof CustomPacket) {
            packetbuffer.writeVarInt(((CustomPacket) packet).getCustomPacketID());
        } else {
            packetbuffer.writeVarInt(getPacketIDByProtocol(packet, protocol));
        }
        packet.write(packetbuffer, protocol);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) return;

        try {
            final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);

            final int packetID = packetBuffer.readVarInt();

            Packet packet = BetterProxy.getInstance().getPacketRegistry().createPacket(connectionState, packetDirection, packetID, protocol);

            if(config.debugger && (config.debugType == DebugType.BYTEARRAY || config.showCustomPackets)) {
                final ByteBuf bufDUPLICATE = byteBuf.duplicate();

                byte[] data = new byte[bufDUPLICATE.readableBytes()];
                bufDUPLICATE.readBytes(data);

                System.err.println("[" + channelHandlerContext.channel().remoteAddress() + "] [size: " + data.length + "] Packet data " + packet.getClass().getSimpleName() + "(" + packetID + "): " + Arrays.toString(data));

                bufDUPLICATE.clear();
            }

            packet.read(packetBuffer, protocol);

            if(config.debugger && config.debugType == DebugType.LEGIBLE && !(packet instanceof CustomPacket)) {
                final boolean showObjectType = false;

                System.err.println("[" + channelHandlerContext.channel().remoteAddress() + "] Packet data " + packet.getClass().getSimpleName() + "(" + packetID + "): " + Arrays.stream(packet.getClass().getDeclaredFields()).map(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(packet);

                        if(value == null || value.toString().isEmpty()) {
                            return field.getName() + ": null";
                        }

                        String name = value.getClass().getSimpleName();
                        if(ArrayUtil.isArray(value)) {
                            return field.getName() + ": " + name.replace("[]", "Array") + "(" + Array.getLength(value) + "): { " + ArrayUtil.getValue(value) + " }";
                        }

                        return field.getName() + ": " + (showObjectType ? "(" + name + "): " : "") +  value;
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                }).collect(Collectors.toList()));
            }

            if (packetBuffer.isReadable()) {
                throw new DecoderException(String.format("Packet (%s) was larger than i expected found %s bytes extra", packet.getClass().getSimpleName(), packetBuffer.readableBytes()));
            }

            list.add(packet);
            byteBuf.clear();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getPacketIDByProtocol(Packet packet, int protocol) {
        for (Protocol p : packet.getProtocolList()) {
            for (int protocol2 : p.getProtocols()) {
                if (protocol2 == protocol) {
                    return p.getId();
                }
            }
        }
        return packet.getProtocolList().get(0).getId();
    }
}