package me.Ikos3k.proxy.protocol.objects;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;
import me.Ikos3k.proxy.handler.ServerHandler;
import me.Ikos3k.proxy.protocol.ProtocolType;
import me.Ikos3k.proxy.protocol.codecs.CompressionCodec;
import me.Ikos3k.proxy.protocol.codecs.PacketCodec;
import me.Ikos3k.proxy.protocol.data.ConnectionState;
import me.Ikos3k.proxy.protocol.packet.Packet;

@Data
public class Session {
    private final Channel channel;
    private ServerHandler packetHandler;
    private String username;

    public void fastSendPacket(Packet p) {
        this.channel.writeAndFlush(p);
    }

    public void sendPacket(Packet p) {
        if (!isChannelOpen()) {
            return;
        }

        if (this.channel.eventLoop().inEventLoop()) {
            this.channel.writeAndFlush(p).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(() -> this.channel.writeAndFlush(p).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE));
        }
    }

    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }

    public void setProtocolID(int protocol) {
        getPacketCodec().setProtocol(protocol);
    }

    public int getProtocolID() {
        if (getPacketCodec() == null) {
            return ProtocolType.PROTOCOL_UNKNOWN.getProtocol();
        }
        return getPacketCodec().getProtocol();
    }

    public ConnectionState getConnectionState() {
        return getPacketCodec().getConnectionState();
    }

    public void setConnectionState(ConnectionState state) {
        getPacketCodec().setConnectionState(state);
    }

    public PacketCodec getPacketCodec() {
        return ((PacketCodec) channel.pipeline().get("packetCodec"));
    }

    public void setCompressionThreshold(final int threshold) {
        if (getConnectionState() == ConnectionState.LOGIN) {
            if (channel.pipeline().get("compression") == null) {
                channel.pipeline().addBefore("packetCodec", "compression", new CompressionCodec(threshold));
            } else {
                ((CompressionCodec) channel.pipeline().get("compression")).setCompressionThreshold(threshold);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
}