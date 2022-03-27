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
import me.Ikos3k.proxy.protocol.objects.Bot;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.objects.Session;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketDirection;
import me.Ikos3k.proxy.protocol.packet.impl.client.HandshakePacket;
import me.Ikos3k.proxy.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientCustomPayloadPacket;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientKeepAlivePacket;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientSettingsPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.login.ServerLoginDisconnectPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerChatPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerDisconnectPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerJoinGamePacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerKeepAlivePacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Data
public class BotConnection {
    private final EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port, Proxy proxy, Bot bot, String username) {
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
                        pipeline.addLast("packetCodec", new PacketCodec(ConnectionState.LOGIN, PacketDirection.CLIENTBOUND));
                        pipeline.addLast("handler", new SimpleChannelInboundHandler<Packet>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                TimeUnit.MILLISECONDS.sleep(150);
                                bot.getSession().sendPacket(new HandshakePacket(bot.getSession().getProtocolID(), host, port, 2));
                                bot.getSession().sendPacket(new ClientLoginStartPacket(username));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                disconnect(ctx.name(), host, port, bot, bot.getOwner());
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
                                    ChatUtil.sendChatMessage(bot.getOwner().getThemeType().getColor(1) + ">> &8Bot " + bot.getOwner().getThemeType().getColor(2) + username + " &8connected to the server &a" + host + ":" + port, bot.getOwner(), true);
                                    bot.getOwner().getBots().add(bot);
                                    bot.getSession().sendPacket(new ClientCustomPayloadPacket("MC|Brand", "vanilla".getBytes()));
                                    bot.getSession().sendPacket(new ClientSettingsPacket("pl_PL", (byte) 32, (byte) 0, false, (byte) 1));
                                } else if (packet instanceof ServerDisconnectPacket) {
                                    disconnect(ChatColor.stripColor(BaseComponent.toLegacyText(((ServerDisconnectPacket) packet).getReason())), host, port, bot, bot.getOwner());
                                } else if (packet instanceof ServerLoginDisconnectPacket) {
                                    disconnect(ChatColor.stripColor(BaseComponent.toLegacyText(((ServerLoginDisconnectPacket) packet).getReason())), host, port, bot, bot.getOwner());
                                }
                            }
                        });
                    }
                });
        bot.setSession(new Session(bootstrap.connect(host, port).syncUninterruptibly().channel()));
        bot.getSession().setProtocolID(bot.getOwner().getSession().getProtocolID());
        bot.getSession().setConnectionState(ConnectionState.LOGIN);
        bot.getSession().setUsername(username);
    }

    private void disconnect(String cause, String host, int port, Bot bot, Player owner) {
        owner.getSession().sendPacket(new ServerChatPacket(new TextComponent(ChatUtil.fixColor(owner.getThemeType().getColor(1) + ">> &8Bot &c" + bot.getUsername() + " &8disconnected from the server &c" + host + ":" + port))
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor(" &fcause: &c" + cause))))));

        bot.getSession().getChannel().close();
        bot.setSession(null);
        owner.getBots().remove(bot);
        group.shutdownGracefully();
    }
}