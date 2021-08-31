package me.ANONIMUS.proxy.protocol.connection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import me.ANONIMUS.proxy.enums.ConnectedType;
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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class PlayerConnection {
    private final Player player;
    private final String username;

    private final EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String ip, int port, Proxy proxy) {
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
                                PacketUtil.clearTabList(player);
                                ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Connecting to server &7[" + player.getThemeType().getColor(1) + ip + "&7]", player, false);
                                if (proxy != Proxy.NO_PROXY) {
                                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Used proxy: " + player.getThemeType().getColor(2) + proxy.address().toString(), player, false);
                                }

                                TimeUnit.MILLISECONDS.sleep(150);
                                player.getRemoteSession().sendPacket(new HandshakePacket(player.getSession().getProtocolID(), ip, port, 2));
                                player.getRemoteSession().sendPacket(new ClientLoginStartPacket(username));
                                player.setServerData(new ServerData(ip, port));
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) {
                                disconnect(ctx.name());
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
                                player.getLastPacket().setReceived(System.currentTimeMillis());
                                player.getLastPacket().setLastReceivedPacket(packet);
                                if (packet instanceof ServerLoginSetCompressionPacket) {
                                    player.getRemoteSession().setCompressionThreshold(((ServerLoginSetCompressionPacket) packet).getThreshold());
                                } else if (packet instanceof ServerLoginSuccessPacket) {
                                    player.getRemoteSession().setConnectionState(ConnectionState.PLAY);
                                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Successfully " + player.getThemeType().getColor(1) + "logged!", player, false);
                                } else if (packet instanceof ServerJoinGamePacket) {
                                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Downloading terrain!", player, false);
                                    WorldUtil.dimSwitch(player, (ServerJoinGamePacket) packet);
                                    player.setConnectedType(ConnectedType.CONNECTED);
                                    ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> Connected successfully&8!", player, false);
                                } else if (packet instanceof ServerDisconnectPacket) {
                                    disconnect(BaseComponent.toLegacyText(((ServerDisconnectPacket) packet).getReason()));
                                } else if (packet instanceof ServerLoginDisconnectPacket) {
                                    disconnect(BaseComponent.toLegacyText(((ServerLoginDisconnectPacket) packet).getReason()));
                                } else if (packet instanceof ServerKeepAlivePacket) {
                                    player.getRemoteSession().sendPacket(new ClientKeepAlivePacket(((ServerKeepAlivePacket) packet).getKeepaliveId()));
                                } else if (packet instanceof ServerCustomPayloadPacket) {
                                    if (((ServerCustomPayloadPacket) packet).getData() != null && ((ServerCustomPayloadPacket) packet).getChannel().equals("MC|Brand")) {
                                        ChatUtil.sendChatMessage(player.getThemeType().getColor(1) + ">> &8Engine: " + player.getThemeType().getColor(1) + ((ServerCustomPayloadPacket) packet).getData().readString().split(" ")[0], player, false);
                                    }
                                } else if (player.isConnected() && player.getRemoteSession().getConnectionState() == ConnectionState.PLAY) {
                                    if (player.isListenChunks() && packet instanceof CustomPacket) {
                                        if ((((player).getSession().getProtocolID() == 47 && ((CustomPacket) packet).getCustomPacketID() == 0x26) ||
                                                ((player).getSession().getProtocolID() != 47 && ((CustomPacket) packet).getCustomPacketID() == 0x20))) {
                                            player.getListenedChunks().add(packet);
                                            PacketUtil.sendTitle(player, "[CHUNKS]", "listening... (" + player.getListenedChunks().size() + ")");
                                        }
                                    }

                                    if (packet instanceof ServerTabCompletePacket) {
                                        if (player.isPlayersState()) {
                                            player.setPlayersState(false);
                                            for (String m : ((ServerTabCompletePacket) packet).getMatches()) {
                                                player.getPlayers().add(m);
                                            }
                                            String out = player.getPlayers().toString();
                                            if (out.equals("[]")) {
                                                ChatUtil.sendChatMessage("&cNo players found!", player, true);
                                                return;
                                            }
                                            ChatUtil.sendChatMessage("&f" + out.replace("[", "").replace("]", "") + " &8[&f" + player.getPlayers().size() + "&8]", player, true);
                                        }

                                        if (player.isPluginsState()) {
                                            player.setPluginsState(false);
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
                                                ChatUtil.sendChatMessage("&cNo players found!", player, true);
                                                return;
                                            }
                                            ChatUtil.sendChatMessage("&f" + out.replace("[", "").replace("]", ""), player, true);
                                        }
                                    }

                                    if (packet instanceof ServerPlayerListEntryPacket) {
                                        for (PlayerListEntry playerListEntry : ((ServerPlayerListEntryPacket) packet).getEntries()) {
                                            if (((ServerPlayerListEntryPacket) packet).getAction() == PlayerListEntryAction.ADD_PLAYER) {
                                                player.getTabList().add(playerListEntry);
                                            } else if (((ServerPlayerListEntryPacket) packet).getAction() == PlayerListEntryAction.REMOVE_PLAYER) {
                                                player.getTabList().remove(playerListEntry);
                                            }
                                        }
                                    }

                                    if (packet instanceof ServerTimeUpdatePacket && player.getTimeType() != TimeType.DEFAULT) {
                                        player.getSession().sendPacket(new ServerTimeUpdatePacket(player.getTimeType().getAge(), player.getTimeType().getTime()));
                                        return;
                                    }

                                    if (packet instanceof ServerPlayerListHeaderFooter && !player.getOptionsManager().getOptionByName("server tablist").isEnabled()) {
                                        return;
                                    }

                                    player.getSession().sendPacket(packet);
                                }
                            }
                        });
                    }
                });
        player.setRemoteSession(new Session(bootstrap.connect(ip, port).syncUninterruptibly().channel()));
        player.getRemoteSession().setProtocolID(player.getSession().getProtocolID());
        player.getRemoteSession().setConnectionState(ConnectionState.LOGIN);
        player.getRemoteSession().setUsername(username);
    }

    private void disconnect(String cause) {
        if(player.getSession() != null) {
            player.getSession().sendPacket(new ServerChatPacket(new TextComponent(ChatUtil.fixColor(player.getThemeType().getColor(1) + ">> &8Connection to the server was lost: " + player.getThemeType().getColor(1) + player.getServerData().getHost() + (cause != null ? " &8cause: " + player.getThemeType().getColor(1) + ChatColor.stripColor(cause) : "")))
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent(ChatUtil.fixColor(player.getThemeType().getColor(1) + "click to reconnect &8[" + player.getThemeType().getColor(2) + player.getServerData().getHost() + (!player.getServerData().getHost().contains(player.getServerData().getIp()) ? "(" + player.getServerData().getIp() + ")&8]" : "")))))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, player.getPrefixCMD() + "join " + player.getServerData().getHost() + " " + username + " false false"))));
        }

        player.getRemoteSession().getChannel().close();
        player.setConnectedType(ConnectedType.DISCONNECTED);
        player.setServerData(null);
        group.shutdownGracefully();
        WorldUtil.lobby(player, true);
    }
}