package me.ANONIMUS.proxy.handler.impl;

import lombok.SneakyThrows;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.handler.ServerHandler;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.ItemStack;
import me.ANONIMUS.proxy.protocol.data.WindowAction;
import me.ANONIMUS.proxy.protocol.data.WindowType;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.*;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerOpenWindowPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerTabCompletePacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerWindowItemsPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;
import me.ANONIMUS.proxy.utils.ItemUtil;
import me.ANONIMUS.proxy.utils.PacketUtil;
import me.ANONIMUS.proxy.utils.SkinUtil;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ServerPlayHandler extends ServerHandler {
    public ServerPlayHandler(Player player) {
        super(player);
    }

    @Override
    public void disconnected() {
        System.out.println("[" + player.getAccount().getUsername() + "] Disconnected.");
        ChatUtil.sendBroadcastMessage(player.getThemeType().getColor(1) + ">> &8The player " + player.getThemeType().getColor(1) + player.getAccount().getUsername() + " &8has disconnected from the " + player.getThemeType().getColor(1) + "BetterProxy&8!", false);
    }

    @SneakyThrows
    @Override
    public void handlePacket(Packet packet) {
        player.getLastPacket().setSent(System.currentTimeMillis());
        player.getLastPacket().setLastSentPacket(packet);
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            if (packet instanceof ClientPlayerPlaceBlockPacket) {
                final ClientPlayerPlaceBlockPacket block = (ClientPlayerPlaceBlockPacket) packet;
                if (!player.isConnected()) {
                    if (block.getHeld() != null) {
                        if (ItemUtil.optionsMenu().getName().equals(block.getHeld().getName())) {
                            List<ItemStack> items = new ArrayList<>();
                            player.getOptionsManager().getOptions().forEach(option -> items.add(ItemUtil.option(option)));
                            player.getSession().sendPacket(new ServerOpenWindowPacket(234, WindowType.CHEST, "SETTINGS", 9));
                            player.getSession().sendPacket(new ServerWindowItemsPacket(234, items));
                            return;
                        }
                        if(ItemUtil.changeSkinMenu(player).getName().equals(block.getHeld().getName())) {
                            List<ItemStack> items = new ArrayList<>();

                            String[] nicknames = new String[] {
                                "Kola13567", "Szumir", "Nyatix"
                            };

                            for(String nick : nicknames) {
                                items.add(ItemUtil.skull(SkinUtil.getSkin(nick, null)));
                            }

                            player.getSession().sendPacket(new ServerOpenWindowPacket(235, WindowType.CHEST, "SKINS", 36));
                            player.getSession().sendPacket(new ServerWindowItemsPacket(235, items));
                            return;
                        }
                    }
                }
            }
            if (packet instanceof ClientPlayerWindowActionPacket) {
                ClientPlayerWindowActionPacket window = (ClientPlayerWindowActionPacket) packet;
                if (window.getWindowId() == -22) {
                    if (window.getMode() != WindowAction.CLICK_ITEM) {
                        PacketUtil.clearInventory(player);
                    }
                    List<ItemStack> items = new ArrayList<>();
                    player.getOptionsManager().getOptions().forEach(options -> {
                        if (window.getItem() != null && window.getMode() == WindowAction.CLICK_ITEM && options.getName().equalsIgnoreCase(ChatColor.stripColor(window.getItem().getName()))) {
                            options.toggle(player);
                        }
                        items.add(ItemUtil.option(options));
                    });
                    if (!player.isConnected()) {
                        ItemUtil.loadStartItems(player);
                    }
                    player.getSession().sendPacket(new ServerOpenWindowPacket(234, WindowType.CHEST, "SETTINGS", 9));
                    player.getSession().sendPacket(new ServerWindowItemsPacket(234, items));
                    return;
                }
                if (window.getWindowId() == -21) {
                    //:p
                    return;
                }
            }
        }
        if (packet instanceof ClientTabCompletePacket) {
            ClientTabCompletePacket tabCompletePacket = (ClientTabCompletePacket) packet;

            if(tabCompletePacket.getText().startsWith(player.getPrefixCMD())) {
                Optional<Command> optionalCommand = BetterProxy.getInstance().getCommandManager().getCommands().stream().filter(cmd -> (player.getPrefixCMD() + cmd.getPrefix()).startsWith(tabCompletePacket.getText())).findFirst();
                if (!optionalCommand.isPresent()) {
                    optionalCommand = BetterProxy.getInstance().getCommandManager().getCommands().stream().filter(cmd -> cmd.getAlias() != null && (player.getPrefixCMD() + cmd.getAlias()).startsWith(tabCompletePacket.getText())).findFirst();
                    optionalCommand.ifPresent(command -> player.getSession().sendPacket(new ServerTabCompletePacket(new String[]{player.getPrefixCMD() + command.getAlias()})));
                } else {
                    player.getSession().sendPacket(new ServerTabCompletePacket(new String[]{player.getPrefixCMD() + optionalCommand.get().getPrefix()}));
                }
            }
        }
        if (packet instanceof ClientPlayerPositionPacket) {
            if (((ClientPlayerPositionPacket) packet).getY() < 60 && !player.isConnected()) {
                PacketUtil.lobbyPosTeleport(player);
            }
        }

        if (packet instanceof ClientChatPacket) {
            final String message = ((ClientChatPacket) packet).getMessage();
            if (message.startsWith(player.getPrefixCMD())) {
                BetterProxy.getInstance().getCommandManager().onCommand(message, player);
            } else if (player.isLogged() && message.startsWith("@")) {
                ChatUtil.sendBroadcastMessage("&8(&f" + ProtocolType.getByProtocolID(player.getSession().getProtocolID()).getPrefix() + "&8) &8[" + player.getAccount().getGroup().getPrefix() + "&8] " + player.getThemeType().getColor(1) + player.getAccount().getUsername() + " &8>> &7" + message.substring(1), false);
            } else {
                forwardPacket(packet);
            }
        } else {
            forwardPacket(packet);
        }
    }

    private void forwardPacket(final Packet packet) {
        if (player.isConnected()) {
            if (player.isMother()) {
                if (player.getMotherDelay() == 0) {
                    player.getBots().forEach(bot ->
                            bot.getSession().sendPacket(packet)
                    );
                } else {
                    int delay = player.getMotherDelay();
                    Thread thread0 = new Thread(() -> player.getBots().forEach(bot -> {
                        Thread thread1 = new Thread(() -> {
                            try {
                                TimeUnit.MILLISECONDS.sleep(delay);
                                bot.getSession().sendPacket(packet);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        thread1.start();
                        try {
                            TimeUnit.MILLISECONDS.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }));
                    thread0.start();
                }
            }
            if (!(packet instanceof ClientKeepAlivePacket))
                player.getRemoteSession().sendPacket(packet);
        }
    }
}
