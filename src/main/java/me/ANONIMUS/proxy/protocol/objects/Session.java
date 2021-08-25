package me.ANONIMUS.proxy.protocol.objects;

import io.netty.channel.Channel;
import lombok.Data;
import me.ANONIMUS.proxy.handler.ServerHandler;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.handlers.CompressionCodec;
import me.ANONIMUS.proxy.protocol.handlers.PacketCodec;
import me.ANONIMUS.proxy.protocol.packet.Packet;

@Data
public class Session {
    private final Channel channel;
    private ServerHandler packetHandler;
    private String username;

    public void sendPacket(Packet p) {
        if (isChannelOpen()) {
            channel.writeAndFlush(p);
        }
    }

    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }

    public void setConnectionState(ConnectionState state) {
        getPacketCodec().setConnectionState(state);
    }

    public void setProtocolID(int protocol) {
        getPacketCodec().setProtocol(protocol);
    }

    public int getProtocolID() {
        if(getPacketCodec() == null) {
            return ProtocolType.PROTOCOL_UNKNOWN.getProtocol();
        }
        return getPacketCodec().getProtocol();
    }

    public ConnectionState getConnectionState() {
        return getPacketCodec().getConnectionState();
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