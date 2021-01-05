package me.ANONIMUS.proxy.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerKeepAlivePacket;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.handlers.PacketCodec;
import me.ANONIMUS.proxy.protocol.handlers.VarInt21FrameEncoder;
import me.ANONIMUS.proxy.protocol.handlers.Varint21FrameDecoder;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Data
public class ProxyServer {
    private final int port;

    EventLoopGroup worker = new NioEventLoopGroup();

    public void bind() {
        new ServerBootstrap()
                .group(worker)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("timer", new ReadTimeoutHandler(10));
                        pipeline.addLast("varintEncoder", new VarInt21FrameEncoder());
                        pipeline.addLast("varintDecoder", new Varint21FrameDecoder());
                        pipeline.addLast("packetCodec", new PacketCodec(ConnectionState.HANDSHAKE, PacketDirection.SERVERBOUND));
                        pipeline.addLast(new SimpleChannelInboundHandler<Packet>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                PlayerManager.createPlayer(new Session(ctx.channel()));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                               PlayerManager.getPlayer(ctx.channel()).disconnected();
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
                                PlayerManager.getPlayer(ctx.channel()).packetReceived(packet);
                            }
                        });
                    }
                }).bind(port);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> PlayerManager.getPlayers().stream().filter(p -> p.getSession().getConnectionState() == ConnectionState.PLAY).forEach(p -> p.getSession().sendPacket(new ServerKeepAlivePacket(System.currentTimeMillis()))),3,3, TimeUnit.SECONDS);
    }
}