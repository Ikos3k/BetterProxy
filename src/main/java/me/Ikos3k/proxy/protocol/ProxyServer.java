package me.Ikos3k.proxy.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import lombok.Data;
import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.handler.impl.ServerLoginHandler;
import me.Ikos3k.proxy.handler.impl.ServerStatusHandler;
import me.Ikos3k.proxy.managers.PlayerManager;
import me.Ikos3k.proxy.protocol.codecs.PacketCodec;
import me.Ikos3k.proxy.protocol.codecs.VarInt21FrameCodec;
import me.Ikos3k.proxy.protocol.data.ConnectionState;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.objects.Session;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketDirection;
import me.Ikos3k.proxy.protocol.packet.impl.client.HandshakePacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerKeepAlivePacket;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Data
public class ProxyServer {
    private final EventLoopGroup worker = new NioEventLoopGroup();
    private final String icon;

    public void bind(PlayerManager playerManager) {
        new ServerBootstrap()
                .group(worker)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("timer", new ReadTimeoutHandler(10));
                        pipeline.addLast("frameCodec", new VarInt21FrameCodec());
                        pipeline.addLast("packetCodec", new PacketCodec(ConnectionState.HANDSHAKE, PacketDirection.SERVERBOUND));
                        pipeline.addLast(new SimpleChannelInboundHandler<Packet>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                playerManager.createPlayer(new Session(ctx.channel()));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                playerManager.getPlayer(ctx.channel()).ifPresent(Player::disconnect);
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
                                Optional<Player> player = playerManager.getPlayer(ctx.channel());
                                if(!player.isPresent()) {
                                    return;
                                }

                                Session session = player.get().getSession();
                                if (packet instanceof HandshakePacket) {
                                    final HandshakePacket handshake = (HandshakePacket) packet;
                                    session.setProtocolID(handshake.getProtocolId());

                                    switch (handshake.getNextState()) {
                                        case 1:
                                            session.setConnectionState(ConnectionState.STATUS);
                                            session.setPacketHandler(new ServerStatusHandler(player.get()));
                                            break;
                                        case 2:
                                            session.setConnectionState(ConnectionState.LOGIN);
                                            session.setPacketHandler(new ServerLoginHandler(player.get()));
                                            break;
                                        default:
                                            session.getChannel().close();
                                            worker.shutdownGracefully();
                                    }
                                } else {
                                    session.getPacketHandler().handlePacket(packet);
                                }
                            }
                        });
                    }
                }).bind(BetterProxy.getInstance().getConfigManager().getConfig().port).addListener((Future<? super Void> future) -> {
                    if (!future.isSuccess()) {
                        worker.shutdownGracefully();
                    }
                });
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> playerManager.elements.stream()
            .filter(p -> p.getSession().getConnectionState() == ConnectionState.PLAY)
            .forEach(p -> p.getSession().sendPacket(new ServerKeepAlivePacket(System.currentTimeMillis()))),
        3, 3, TimeUnit.SECONDS);
    }
}