package me.ANONIMUS.proxy.protocol.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.data.status.ServerStatusInfo;
import me.ANONIMUS.proxy.protocol.handlers.PacketCodec;
import me.ANONIMUS.proxy.protocol.handlers.VarInt21FrameCodec;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.status.ClientStatusRequestPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.status.ServerStatusPongPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.status.ServerStatusResponsePacket;
import me.ANONIMUS.proxy.utils.ChatUtil;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Data
public class ServerPinger {
    private final Player owner;
    private final boolean showResult;

    private Session session;

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port, Proxy proxy) {
        final Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        if (proxy != Proxy.NO_PROXY) {
                            pipeline.addFirst(new Socks4ProxyHandler(proxy.address()));
                        }
                        pipeline.addLast("timer", new ReadTimeoutHandler(20));
                        pipeline.addLast("frameCodec", new VarInt21FrameCodec());
                        pipeline.addLast("packetCodec", new PacketCodec(ConnectionState.STATUS, PacketDirection.CLIENTBOUND));
                        pipeline.addLast("handler", new SimpleChannelInboundHandler<Packet>() {

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                session.getChannel().close();
                                group.shutdownGracefully();
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                if (showResult) {
                                    ChatUtil.sendChatMessage("&aPinging...", owner, true);
                                }
                                TimeUnit.MILLISECONDS.sleep(150);
                                session.sendPacket(new HandshakePacket(session.getProtocolID(), host, port, 1));
                                session.sendPacket(new ClientStatusRequestPacket());
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                                if (packet instanceof ServerStatusResponsePacket) {
                                    final ServerStatusInfo info = ((ServerStatusResponsePacket) packet).getInfo();
                                    ChatUtil.sendChatMessage("&7Max players: &6" + info.getPlayerInfo().getMaxPlayers(), owner, false);
                                    ChatUtil.sendChatMessage("&7Online players: &6" + info.getPlayerInfo().getOnlinePlayers(), owner, false);
                                    ChatUtil.sendChatMessage("&7MOTD: &6" + GsonComponentSerializer.gson().serialize(info.getDescription()), owner, false);
                                    ChatUtil.sendChatMessage("&7Version: &6" + info.getVersionInfo().getVersionName() + "(" + info.getVersionInfo().getProtocolVersion() + ")", owner, false);
                                    session.getChannel().close();
                                    group.shutdownGracefully();
                                } else if (packet instanceof ServerStatusPongPacket) {
                                    session.getChannel().close();
                                    group.shutdownGracefully();
                                }
                            }
                        });
                    }
                });
        session = new Session(bootstrap.connect(host, port).syncUninterruptibly().channel());
        session.setProtocolID(owner.getSession().getProtocolID());
        session.getChannel().config().setOption(ChannelOption.TCP_NODELAY, true);
        session.getChannel().config().setOption(ChannelOption.IP_TOS, 0x18);
    }
}