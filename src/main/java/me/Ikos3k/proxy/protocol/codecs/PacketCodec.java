package me.Ikos3k.proxy.protocol.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import lombok.Getter;
import lombok.Setter;
import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.objects.Config;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.ConnectionState;
import me.Ikos3k.proxy.protocol.data.DebugType;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;
import me.Ikos3k.proxy.protocol.packet.PacketDirection;
import me.Ikos3k.proxy.protocol.packet.impl.CustomPacket;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PacketCodec extends ByteToMessageCodec<Packet> {
    private final PacketDirection packetDirection;
    private ConnectionState connectionState;
    private int protocol;

    private final Config config;

    public PacketCodec(ConnectionState connectionState, PacketDirection packetDirection) {
        this.connectionState = connectionState;
        this.packetDirection = packetDirection;

        this.config = BetterProxy.getInstance().getConfigManager().getConfig();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) {
        if (!byteBuf.isWritable()) return;

        final PacketBuffer packetbuffer = new PacketBuffer(byteBuf);

        if (packet instanceof CustomPacket) {
            packetbuffer.writeVarInt(((CustomPacket) packet).getCustomPacketID());
        } else {
            packetbuffer.writeVarInt(getPacketIDByProtocol(packet, protocol));
        }

        try {
            packet.write(packetbuffer, protocol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) return;

        final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);

        final int packetID = packetBuffer.readVarInt();

        Packet packet = BetterProxy.getInstance().getPacketRegistry().createPacket(connectionState, packetDirection, packetID, protocol);

        if ((packet instanceof CustomPacket || config.debugType == DebugType.BYTEARRAY) && config.showCustomPackets && config.debugger) {
            final ByteBuf bufDUPLICATE = byteBuf.duplicate();

            byte[] data = new byte[bufDUPLICATE.readableBytes()];
            bufDUPLICATE.readBytes(data);

            if(data.length < 200) {
                System.err.println("[" + channelHandlerContext.channel().remoteAddress() + "] [size: " + data.length + "] Packet data " + packet.getClass().getSimpleName() + "(" + packetID + "): " + Arrays.toString(data));
            }

            bufDUPLICATE.clear();
        }

        try {
            packet.read(packetBuffer, protocol);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        if (config.debugger && config.debugType == DebugType.LEGIBLE && !(packet instanceof CustomPacket)) {
            System.err.println("[" + channelHandlerContext.channel().remoteAddress() + "] Packet data " + packet);
        }

        if (packetBuffer.isReadable()) {
            throw new DecoderException(String.format("Packet (%s) was larger than i expected found %s bytes extra", packet.getClass().getSimpleName(), packetBuffer.readableBytes()));
        }

        list.add(packet);
        byteBuf.clear();
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