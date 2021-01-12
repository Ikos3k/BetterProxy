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
import me.ANONIMUS.proxy.objects.ServerData;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.handlers.PacketCodec;
import me.ANONIMUS.proxy.protocol.handlers.VarInt21FrameCodec;
import me.ANONIMUS.proxy.protocol.objects.Bot;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientCustomPayloadPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientKeepAlivePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientSettingsPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginDisconnectPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerDisconnectPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerJoinGamePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerKeepAlivePacket;
import me.ANONIMUS.proxy.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Data
@RequiredArgsConstructor
public class BotConnection {
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port, Proxy proxy, Bot bot) {
        final Bootstrap bootstrap = new Bootstrap()
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
                        pipeline.addLast("packetCodec", new PacketCodec(ConnectionState.LOGIN, PacketDirection.CLIENTBOUND));
                        pipeline.addLast("handler", new SimpleChannelInboundHandler<Packet>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                TimeUnit.MILLISECONDS.sleep(150);
                                bot.getSession().sendPacket(new HandshakePacket(bot.getSession().getProtocolID(), "", port, 2));
                                bot.getSession().sendPacket(new ClientLoginStartPacket(bot.getUsername()));
                                bot.setServerData(new ServerData(host, port));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                ChatUtil.sendChatMessage("&6>> &8Bot &c" + bot.getUsername() + " &8disconnected from the server &c" + host + ":" + port + " &fcause: &c" + ctx.getClass(), bot.getOwner(), true);
                                disconnect(bot, bot.getOwner());
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                                if (packet instanceof ServerLoginSetCompressionPacket) {
                                    bot.getSession().setCompressionThreshold(((ServerLoginSetCompressionPacket) packet).getThreshold());
                                } else if (packet instanceof ServerLoginSuccessPacket) {
                                    bot.getSession().setConnectionState(ConnectionState.PLAY);
                                } else if (packet instanceof ServerKeepAlivePacket) {
                                    bot.getSession().sendPacket(new ClientKeepAlivePacket(((ServerKeepAlivePacket) packet).getKeepaliveId()));
                                } else if (packet instanceof ServerJoinGamePacket) {
                                    ChatUtil.sendChatMessage("&6>> &8Bot &a" + bot.getUsername() + " &8connected to the server &a" + host + ":" + port, bot.getOwner(), true);
                                    bot.getOwner().getBots().add(bot);
                                    bot.getSession().sendPacket(new ClientCustomPayloadPacket("MC|Brand", "vanilla".getBytes()));
                                    bot.getSession().sendPacket(new ClientSettingsPacket("pl_PL", (byte) 32, (byte) 0, false, (byte) 1));
                                } else if (packet instanceof ServerDisconnectPacket) {
                                    ChatUtil.sendChatMessage("&6>> &8Bot &c" + bot.getUsername() + " &8disconnected from the server &c" + host + ":" + port + " &fcause: &c" + ChatColor.stripColor(BaseComponent.toLegacyText(((ServerDisconnectPacket) packet).getReason())), bot.getOwner(), true);
                                    disconnect(bot, bot.getOwner());
                                } else if (packet instanceof ServerLoginDisconnectPacket) {
                                    ChatUtil.sendChatMessage("&6>> &8Bot &c" + bot.getUsername() + " &8disconnected from the server &c" + host + ":" + port + " &cause: &c" + ChatColor.stripColor(BaseComponent.toLegacyText(((ServerLoginDisconnectPacket) packet).getReason())), bot.getOwner(), true);
                                    disconnect(bot, bot.getOwner());
                                }
                            }
                        });
                    }
                });
        bot.setSession(new Session(bootstrap.connect(host, port).syncUninterruptibly().channel()));
        bot.getSession().setProtocolID(bot.getOwner().getSession().getProtocolID());
        bot.getSession().setConnectionState(ConnectionState.LOGIN);
    }

    private void disconnect(Bot bot, Player owner) {
        bot.getSession().getChannel().close();
        bot.setSession(null);
        owner.getBots().remove(bot);
        group.shutdownGracefully();
    }
}