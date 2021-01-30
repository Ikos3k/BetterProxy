package me.ANONIMUS.proxy.protocol.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import lombok.Getter;
import lombok.Setter;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PacketCodec extends ByteToMessageCodec<Packet> {
    private ConnectionState connectionState;
    private PacketDirection packetDirection;
    private int protocol;

    public PacketCodec(ConnectionState connectionState,PacketDirection packetDirection) {
        this.connectionState = connectionState;
        this.packetDirection = packetDirection;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        final PacketBuffer packetbuffer = new PacketBuffer(byteBuf);
        packetbuffer.writeVarInt(getPacketIDByProtocol(packet, protocol));
        packet.write(packetbuffer, protocol);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) return;

        try {
            final PacketBuffer packetBuffer = new PacketBuffer(byteBuf);

            final int packetID = packetBuffer.readVarInt();

            Packet packet = BetterProxy.getInstance().getPacketRegistry().createPacket(connectionState,packetDirection,packetID,protocol);

            if(BetterProxy.getInstance().getConfigManager().getConfig().debug) {
                final ByteBuf bufDUPLICATE = byteBuf.duplicate();
                final byte[] data = new byte[bufDUPLICATE.readableBytes()];
                bufDUPLICATE.readBytes(data);
                if(data.length > 1) {
                    System.err.println("[" + channelHandlerContext.channel().remoteAddress() + "] Packet data " + packet.getClass().getSimpleName() + "(" + packetID + "): " + Arrays.toString(data));
                }
                bufDUPLICATE.clear();
            }

            packet.read(packetBuffer, protocol);

            if (packetBuffer.isReadable()) {
                throw new DecoderException(String.format("Packet (%s) was larger than i expected found %s bytes extra",packet.getClass().getSimpleName(),packetBuffer.readableBytes()));
            }
            list.add(packet);
            byteBuf.clear();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getPacketIDByProtocol(Packet packet, int protocol) {
        if(packet instanceof CustomPacket) { return ((CustomPacket)packet).getCustomPacketID(); }
        for(Protocol p : packet.getProtocolList()) {
            if(p.getProtocol() == protocol) {
                return p.getId();
            }
        }
        return packet.getProtocolList().get(0).getId();
    }
}