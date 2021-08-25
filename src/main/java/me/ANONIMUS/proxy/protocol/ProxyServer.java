package me.ANONIMUS.proxy.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.handler.impl.ServerLoginHandler;
import me.ANONIMUS.proxy.handler.impl.ServerStatusHandler;
import me.ANONIMUS.proxy.managers.PlayerManager;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.handlers.PacketCodec;
import me.ANONIMUS.proxy.protocol.handlers.VarInt21FrameCodec;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;

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
                            playerManager.getPlayer(ctx.channel()).disconnect();
                        }

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
                            Player player = playerManager.getPlayer(ctx.channel());
                            Session session = player.getSession();
                            if (packet instanceof HandshakePacket) {
                                final HandshakePacket handshake = (HandshakePacket) packet;
                                session.setProtocolID(handshake.getProtocolId());
                                switch (handshake.getNextState()) {
                                    case 1:
                                        session.setConnectionState(ConnectionState.STATUS);
                                        session.setPacketHandler(new ServerStatusHandler(player));
                                        break;
                                    case 2:
                                        session.setConnectionState(ConnectionState.LOGIN);
                                        session.setPacketHandler(new ServerLoginHandler(player));
                                        break;
                                }
                            } else {
                                session.getPacketHandler().handlePacket(packet);
                            }
                        }
                    });
                }
            }).bind(BetterProxy.getInstance().getConfigManager().getConfig().port);
    }
}