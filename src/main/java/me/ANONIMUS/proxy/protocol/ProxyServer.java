package me.ANONIMUS.proxy.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.handlers.PacketCodec;
import me.ANONIMUS.proxy.protocol.handlers.VarInt21FrameCodec;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerKeepAlivePacket;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Data
public class ProxyServer {
    EventLoopGroup worker = new NioEventLoopGroup();

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
                                playerManager.getPlayer(ctx.channel()).disconnect();
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
                                playerManager.getPlayer(ctx.channel()).packetReceived(packet);
                            }
                        });
                    }
                }).bind(BetterProxy.getInstance().getConfigManager().getConfig().port);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> playerManager.getPlayers().stream().filter(p -> p.getSession().getConnectionState() == ConnectionState.PLAY).forEach(p -> p.getSession().sendPacket(new ServerKeepAlivePacket(System.currentTimeMillis()))), 3, 3, TimeUnit.SECONDS);
    }

    public String getIcon() {
        return icon;
    }
}