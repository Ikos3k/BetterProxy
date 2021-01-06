package me.ANONIMUS.proxy.command.impl.normal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import me.ANONIMUS.proxy.command.Command;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.objects.Bot;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
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
import me.ANONIMUS.proxy.utils.WorldUtil;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class CommandStay extends Command {
    public CommandStay() {
        super("stay", "detach", "", "", CommandType.MORE, ConnectedType.CONNECTED);
    }

    EventLoopGroup group = new NioEventLoopGroup();

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        final Bot bot = new Bot(sender.getRemoteSession().getUsername(), sender);

        sender.getRemoteSession().getChannel().pipeline().replace("handler", "handler", new SimpleChannelInboundHandler<Packet>() {
            @Override
            public void channelInactive(ChannelHandlerContext ctx) {
                ChatUtil.sendChatMessage("&6>> &8Bot &c" + bot.getUsername() + " &8disconnected from the server &c" + bot.getServerData().getHost() + ":" + bot.getServerData().getPort() + " &fcause: &c" + ctx.getClass(), bot.getOwner(), true);
                disconnect(bot, sender);
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
                    ChatUtil.sendChatMessage("&6>> &8Bot &a" + bot.getUsername() + " &8connected to the server &a" + bot.getServerData().getHost() + ":" + bot.getServerData().getPort(), bot.getOwner(), true);
                    bot.getOwner().getBots().add(bot);
                    bot.getSession().sendPacket(new ClientCustomPayloadPacket("MC|Brand", "vanilla".getBytes()));
                    bot.getSession().sendPacket(new ClientSettingsPacket("pl_PL", (byte) 32, (byte) 0, false, (byte) 1));
                } else if (packet instanceof ServerDisconnectPacket) {
                    ChatUtil.sendChatMessage("&6>> &8Bot &c" + bot.getUsername() + " &8disconnected from the server &c" + bot.getServerData().getHost() + ":" + bot.getServerData().getPort() + " &fcause: &c" + ChatUtil.stripColor(GsonComponentSerializer.gson().serialize(((ServerDisconnectPacket) packet).getReason())), bot.getOwner(), true);
                    disconnect(bot, sender);

                } else if (packet instanceof ServerLoginDisconnectPacket) {
                    ChatUtil.sendChatMessage("&6>> &8Bot &c" + bot.getUsername() + " &8disconnected from the server &c" + bot.getServerData().getHost() + ":" + bot.getServerData().getPort() + " &cause: &c" + ChatUtil.stripColor(GsonComponentSerializer.gson().serialize(((ServerLoginDisconnectPacket) packet).getReason())), bot.getOwner(), true);
                    disconnect(bot, sender);
                }
            }
        });

        bot.setSession(sender.getRemoteSession());
        bot.setServerData(sender.getServerData());
        sender.getBots().add(bot);

        sender.setRemoteSession(null);
        sender.setConnected(false);
        sender.setServerData(null);
        WorldUtil.lobby(sender, true);
    }

    private void disconnect(Bot bot, Player owner) {
        bot.getSession().getChannel().close();
        bot.setSession(null);
        owner.getBots().remove(bot);
        group.shutdownGracefully();
    }
}