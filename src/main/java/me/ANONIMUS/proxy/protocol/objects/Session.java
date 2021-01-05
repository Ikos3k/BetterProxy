package me.ANONIMUS.proxy.protocol.objects;

import io.netty.channel.Channel;
import lombok.Data;
import me.ANONIMUS.proxy.handler.ServerHandler;
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
        if(channel.isOpen()) { channel.writeAndFlush(p); }
    }

    public void setConnectionState(ConnectionState state) {
        ((PacketCodec) channel.pipeline().get("packetCodec")).setConnectionState(state);
    }

    public ConnectionState getConnectionState() {
        return ((PacketCodec) channel.pipeline().get("packetCodec")).getConnectionState();
    }

    public void setCompressionThreshold(final int threshold) {
        if(getConnectionState() == ConnectionState.LOGIN) {
            if (channel.pipeline().get("compression") == null) {
                channel.pipeline().addBefore("packetCodec", "compression", new CompressionCodec(threshold));
            } else {
                ((CompressionCodec) channel.pipeline().get("compression")).setCompressionThreshold(threshold);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setProtocolID(int protocol) {
        ((PacketCodec) channel.pipeline().get("packetCodec")).setProtocol(protocol);
    }

    public int getProtocolID() {
        return ((PacketCodec) channel.pipeline().get("packetCodec")).getProtocol();
    }
}