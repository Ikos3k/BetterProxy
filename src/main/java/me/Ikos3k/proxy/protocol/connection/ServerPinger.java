package me.Ikos3k.proxy.protocol.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import me.Ikos3k.proxy.protocol.codecs.PacketCodec;
import me.Ikos3k.proxy.protocol.codecs.VarInt21FrameCodec;
import me.Ikos3k.proxy.protocol.data.ConnectionState;
import me.Ikos3k.proxy.protocol.data.status.ServerStatusInfo;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.objects.Session;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketDirection;
import me.Ikos3k.proxy.protocol.packet.impl.client.HandshakePacket;
import me.Ikos3k.proxy.protocol.packet.impl.client.status.ClientStatusRequestPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.status.ServerStatusResponsePacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Data
public class ServerPinger {
    private final Player owner;
    private final boolean showResult;
    private Session session;

    private final EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port, Proxy proxy) {
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.IP_TOS, 0x18)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        if (proxy != Proxy.NO_PROXY) {
                            pipeline.addFirst(new Socks4ProxyHandler(proxy.address()));
                        }
                        pipeline.addLast("timer", new ReadTimeoutHandler(30));
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
                                    ChatUtil.sendChatMessage(owner.getThemeType().getColor(2) + "Pinging...", owner, true);
                                }
                                TimeUnit.MILLISECONDS.sleep(150);
                                session.sendPacket(new HandshakePacket(session.getProtocolID(), host, port, 1));
                                session.sendPacket(new ClientStatusRequestPacket());
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                                if (packet instanceof ServerStatusResponsePacket && showResult) {
                                    final ServerStatusInfo info = ((ServerStatusResponsePacket) packet).getInfo();
                                    ChatUtil.sendChatMessage("&7Max players: " + owner.getThemeType().getColor(1) + info.getPlayerInfo().getMaxPlayers(), owner, false);
                                    ChatUtil.sendChatMessage("&7Online players: " + owner.getThemeType().getColor(1) + info.getPlayerInfo().getOnlinePlayers(), owner, false);
                                    ChatUtil.sendChatMessage("&7MOTD: " + owner.getThemeType().getColor(1) + BaseComponent.toLegacyText(info.getDescription()), owner, false);
                                    ChatUtil.sendChatMessage("&7Version: " + owner.getThemeType().getColor(1) + info.getVersionInfo().getVersionName() + "(" + info.getVersionInfo().getProtocolVersion() + ")", owner, false);
                                }

                                session.getChannel().close();
                                group.shutdownGracefully();
                            }
                        });
                    }
                });
        session = new Session(bootstrap.connect(host, port).syncUninterruptibly().channel());
        session.setProtocolID(owner.getSession().getProtocolID());
    }
}