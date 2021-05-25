package me.ANONIMUS.proxy.protocol.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import me.ANONIMUS.proxy.enums.TimeType;
import me.ANONIMUS.proxy.objects.ServerData;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.handlers.PacketCodec;
import me.ANONIMUS.proxy.protocol.handlers.VarInt21FrameCodec;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketDirection;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.login.ClientLoginStartPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientKeepAlivePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginDisconnectPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSetCompressionPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.login.ServerLoginSuccessPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.*;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.PacketUtil;
import me.ANONIMUS.proxy.utils.WorldUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class PlayerConnection {
    private final Player owner;
    private final String username;

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String ip, int port, Proxy proxy) {
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
                                PacketUtil.clearTabList(owner);
                                ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> &8Connecting to server &7[" + owner.getThemeType().getColor(1) + ip + "&7]", owner, false);
                                if (proxy != Proxy.NO_PROXY) {
                                    ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> &8Used proxy: " + owner.getThemeType().getColor(2) + proxy.address().toString(), owner, false);
                                }
                                TimeUnit.MILLISECONDS.sleep(150);
                                owner.getRemoteSession().sendPacket(new HandshakePacket(owner.getSession().getProtocolID(), ip, port, 2));
                                owner.getRemoteSession().sendPacket(new ClientLoginStartPacket(username));
                                owner.setServerData(new ServerData(ip, port));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                if (owner.isConnected()) {
                                    ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> &cDisconnected!", owner, false);
                                    owner.setConnected(false);
                                    WorldUtil.lobby(owner, true);
                                }
                                owner.setRemoteSession(null);
                                owner.setServerData(null);
                                group.shutdownGracefully();
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                                owner.getLastPacket().setReceived(System.currentTimeMillis());
                                owner.getLastPacket().setLastReceivedPacket(packet);
                                if (packet instanceof ServerLoginSetCompressionPacket) {
                                    owner.getRemoteSession().setCompressionThreshold(((ServerLoginSetCompressionPacket) packet).getThreshold());
                                } else if (packet instanceof ServerLoginSuccessPacket) {
                                    owner.getRemoteSession().setConnectionState(ConnectionState.PLAY);
                                    ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> &8Successfully " + owner.getThemeType().getColor(1) + "logged!", owner, false);
                                } else if (packet instanceof ServerJoinGamePacket) {
                                    ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> &8Downloading terrain!", owner, false);
                                    WorldUtil.dimSwitch(owner, (ServerJoinGamePacket) packet);
                                    owner.setConnected(true);
                                    ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> Connected successfully&8!", owner, false);
                                } else if (packet instanceof ServerDisconnectPacket) {
                                    disconnect(BaseComponent.toLegacyText(((ServerDisconnectPacket) packet).getReason()));
                                } else if (packet instanceof ServerLoginDisconnectPacket) {
                                    disconnect(BaseComponent.toLegacyText(((ServerLoginDisconnectPacket) packet).getReason()));
                                } else if (packet instanceof ServerKeepAlivePacket) {
                                    owner.getRemoteSession().sendPacket(new ClientKeepAlivePacket(((ServerKeepAlivePacket) packet).getKeepaliveId()));
                                } else if (packet instanceof ServerCustomPayloadPacket) {
                                    if (((ServerCustomPayloadPacket) packet).getChannel().equals("MC|Brand")) {
                                        ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> &8Engine: " + owner.getThemeType().getColor(1) + ((ServerCustomPayloadPacket) packet).getData().readString().split(" ")[0], owner, false);
                                    }
                                } else if (owner.isConnected() && owner.getRemoteSession().getConnectionState() == ConnectionState.PLAY) {
                                    if (owner.isListenChunks() && packet instanceof CustomPacket) {
                                        if ((((owner).getSession().getProtocolID() == 47 && ((CustomPacket) packet).getCustomPacketID() == 0x26) ||
                                            ((owner).getSession().getProtocolID() != 47 && ((CustomPacket) packet).getCustomPacketID() == 0x20))) {
                                            owner.getListenedChunks().add(packet);
                                            PacketUtil.sendTitle(owner, "[CHUNKS]", "listening... (" + owner.getListenedChunks().size() + ")");
                                        }
                                    }
                                    if (packet instanceof ServerTabCompletePacket) {
                                        if (owner.isPlayersState()) {
                                            for (String m : ((ServerTabCompletePacket) packet).getMatches()) {
                                                owner.getPlayers().add(m);
                                            }
                                            String out = owner.getPlayers().toString();
                                            if (out.equals("[]")) {
                                                ChatUtil.sendChatMessage("&cNo players found!", owner, true);
                                                owner.setPlayersState(false);
                                                return;
                                            }
                                            ChatUtil.sendChatMessage("&f" + out.replace("[", "").replace("]", "") + " &8[&f" + owner.getPlayers().size() + "&8]", owner, true);
                                            owner.setPlayersState(false);
                                        }
                                        if (owner.isPluginsState()) {
                                            List<String> matches = new ArrayList<>();
                                            for (String m : ((ServerTabCompletePacket) packet).getMatches()) {
                                                if (m.contains(":")) {
                                                    final String[] split = m.split(":", 2);
                                                    String plugin = split[0].replace("/", "");
                                                    if (!matches.contains(plugin)) {
                                                        matches.add(plugin);
                                                    }
                                                }
                                            }
                                            String out = matches.toString();
                                            if (out.equals("[]")) {
                                                ChatUtil.sendChatMessage("&cNo players found!", owner, true);
                                                owner.setPluginsState(false);
                                                return;
                                            }
                                            ChatUtil.sendChatMessage("&f" + out.replace("[", "").replace("]", ""), owner, true);
                                            owner.setPluginsState(false);
                                        }
                                    }
                                    if (packet instanceof ServerPlayerListEntryPacket) {
                                        for (PlayerListEntry playerListEntry : ((ServerPlayerListEntryPacket) packet).getEntries()) {
                                            if (((ServerPlayerListEntryPacket) packet).getAction() == PlayerListEntryAction.ADD_PLAYER) {
                                                owner.getTabList().add(playerListEntry);
                                            } else if (((ServerPlayerListEntryPacket) packet).getAction() == PlayerListEntryAction.REMOVE_PLAYER) {
                                                owner.getTabList().remove(playerListEntry);
                                            }
                                        }
                                    }

                                    if(!owner.getOptionsManager().getOptionByName("server tablist").isEnabled() && packet instanceof ServerPlayerListHeaderFooter) {
                                        return;
                                    }

                                    if (packet instanceof ServerTimeUpdatePacket) {
                                        if (owner.getTimeType() != TimeType.DEFAULT) {
                                            owner.getSession().sendPacket(new ServerTimeUpdatePacket(owner.getTimeType().getAge(), owner.getTimeType().getTime()));
                                            return;
                                        }
                                    }
                                    owner.getSession().sendPacket(packet);
                                }
                            }
                        });
                    }
                });
        owner.setRemoteSession(new Session(bootstrap.connect(ip, port).syncUninterruptibly().channel()));
        owner.getRemoteSession().setProtocolID(owner.getSession().getProtocolID());
        owner.getRemoteSession().setConnectionState(ConnectionState.LOGIN);
        owner.getRemoteSession().setUsername(username);
    }

    private void disconnect(String cause) {
        ChatUtil.sendChatMessage(owner.getThemeType().getColor(1) + ">> &8Connection to the server was lost: " + owner.getThemeType().getColor(1) + owner.getServerData().getHost() + " &8cause: " + owner.getThemeType().getColor(1) + ChatColor.stripColor(cause), owner, false);

        owner.getRemoteSession().getChannel().close();
        owner.setConnected(false);
        owner.setRemoteSession(null);
        owner.setServerData(null);
        group.shutdownGracefully();
        WorldUtil.lobby(owner, true);
    }
}