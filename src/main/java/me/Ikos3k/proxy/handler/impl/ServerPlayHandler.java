package me.Ikos3k.proxy.handler.impl;

import lombok.SneakyThrows;
import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.handler.ServerHandler;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.objects.Macro;
import me.Ikos3k.proxy.protocol.ProtocolType;
import me.Ikos3k.proxy.protocol.data.ItemStack;
import me.Ikos3k.proxy.protocol.data.Position;
import me.Ikos3k.proxy.protocol.data.WindowAction;
import me.Ikos3k.proxy.protocol.data.WindowType;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;
import me.Ikos3k.proxy.protocol.packet.impl.CustomPacket;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.*;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerDestroyEntitiesPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerOpenWindowPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerTabCompletePacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerWindowItemsPacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.ItemUtil;
import me.Ikos3k.proxy.utils.PacketUtil;
import me.Ikos3k.proxy.utils.SkinUtil;
import me.Ikos3k.proxy.utils.inventory.Inventory;
import me.Ikos3k.proxy.utils.inventory.InventoryUtil;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ServerPlayHandler extends ServerHandler {
    public ServerPlayHandler(Player player) {
        super(player);
    }

    @Override
    public void disconnect() {
        System.out.println("[" + player.getUsername() + "] Disconnected.");
        ChatUtil.sendBroadcastMessage(player.getThemeType().getColor(1) + ">> &8The player " + player.getThemeType().getColor(1) + player.getUsername() + " &8has disconnected from the " + player.getThemeType().getColor(1) + "BetterProxy&8!", false);
    }

    @SneakyThrows
    @Override
    public void handlePacket(Packet packet) {
        player.getLastPacket().setSentValue(packet);

        if(packet instanceof ClientCloseWindowPacket) {
            if(player.getCurrentInventory() != null) {
                player.getCurrentInventory().onClose();

                if(!player.getCurrentInventory().isClosable()) {
                    InventoryUtil.reopenInventory(player);
                }
            }
        }
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            if (packet instanceof ClientPlayerPlaceBlockPacket) {
                final ClientPlayerPlaceBlockPacket block = (ClientPlayerPlaceBlockPacket) packet;
                if (!player.isConnected()) {
                    if (block.getHeld() != null) {
                        if (ItemUtil.optionsMenu().getName().equals(block.getHeld().getName())) {
                            List<ItemStack> items = new ArrayList<>();
                            player.getOptionsManager().elements.forEach(option -> items.add(ItemUtil.option(option)));
                            player.getSession().sendPacket(new ServerOpenWindowPacket(234, WindowType.CHEST, "SETTINGS", 9));
                            player.getSession().sendPacket(new ServerWindowItemsPacket(234, items));
                            return;
                        }
                        if (ItemUtil.changeSkinMenu(player).getName().equals(block.getHeld().getName())) {
                            InventoryUtil.openInventory(new Inventory(ChatUtil.fixColor("&6SKINS"), true) {
                                @Override
                                public void init() {
                                    this.items.addAll(Arrays.asList(ItemUtil.playerSkulls("&4Kola13567", "&6Szumir", "&2Nyatix")));

                                    this.setItem(ItemUtil.skull(null).setName("DEFAULT"), getSlots());
                                }

                                @Override
                                public void onAction(WindowAction action, ItemStack itemStack, int slot, int button) {
                                    if(itemStack != null) {
                                        InventoryUtil.closeInventory(player);
                                        if(itemStack.getName().equalsIgnoreCase("DEFAULT")) {
                                            player.updateSkin(SkinUtil.getSkin(player.getUsername()));
                                        } else {
                                            player.updateSkin(SkinUtil.getSkin(ChatColor.stripColor(itemStack.getName())));
                                        }
                                    }
                                }
                            }, player);
                            return;
                        }
                    }
                }
            }
            if (packet instanceof ClientPlayerWindowActionPacket) {
                ClientPlayerWindowActionPacket window = (ClientPlayerWindowActionPacket) packet;

                if(player.getCurrentInventory() != null) {
                    if(player.getCurrentInventory().getSlots() == -999) {
                        player.getCurrentInventory().onClickOutsideGui(window.getButton());
                    }

                    if(player.getCurrentInventory().getWindowID() == window.getWindowId()) {
                        player.getCurrentInventory().onAction(window.getMode(), window.getItem(), window.getSlot(), window.getButton());
                    } else if (player.getCurrentInventory().getWindowID() != window.getWindowId() || window.getSlot() > player.getCurrentInventory().getSlots()) {
                        player.getCurrentInventory().onClickAnotherGui(window.getMode(), window.getItem(), window.getSlot(), window.getButton());
                    }
                }

                if (window.getWindowId() == -22) {
                    if (window.getMode() != WindowAction.CLICK_ITEM) {
                        PacketUtil.clearInventory(player);
                    }
                    List<ItemStack> items = new ArrayList<>();
                    player.getOptionsManager().elements.forEach(options -> {
                        if (window.getItem() != null && window.getMode() == WindowAction.CLICK_ITEM && options.getName().equalsIgnoreCase(ChatColor.stripColor(window.getItem().getName()))) {
                            options.toggle();
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
            }
        }
        if (packet instanceof ClientTabCompletePacket) {
            ClientTabCompletePacket tabCompletePacket = (ClientTabCompletePacket) packet;

            if (tabCompletePacket.getText().startsWith(player.getPrefixCMD())) {
                Optional<Command> optionalCommand = BetterProxy.getInstance().getCommandManager().elements.stream().filter(cmd -> (player.getPrefixCMD() + cmd.getPrefix()).startsWith(tabCompletePacket.getText())).findFirst();
                if (!optionalCommand.isPresent()) {
                    optionalCommand = BetterProxy.getInstance().getCommandManager().elements.stream().filter(cmd -> cmd.getAlias() != null && (player.getPrefixCMD() + cmd.getAlias()).startsWith(tabCompletePacket.getText())).findFirst();
                    optionalCommand.ifPresent(command -> player.getSession().sendPacket(new ServerTabCompletePacket(new String[]{player.getPrefixCMD() + command.getAlias()})));
                } else {
                    player.getSession().sendPacket(new ServerTabCompletePacket(new String[]{player.getPrefixCMD() + optionalCommand.get().getPrefix()}));
                }
            }
        } else if (packet instanceof ClientPlayerPositionPacket) {
            if (!player.isConnected() && ((ClientPlayerPositionPacket) packet).getY() < 65) {
                PacketUtil.lobbyPosTeleport(player);
            } else if (player.isConnected() && !player.isFreecam()) {
                player.setPos(new Position(((ClientPlayerPositionPacket) packet).getX(), ((ClientPlayerPositionPacket) packet).getY(), ((ClientPlayerPositionPacket) packet).getZ()));
            }
        } else if (packet instanceof ClientChatPacket) {
            final String message = ((ClientChatPacket) packet).getMessage();
            if (message.startsWith(player.getPrefixCMD())) {
                BetterProxy.getInstance().getCommandManager().onCommand(message, player);
                return;
            } else if (player.isLogged() && message.startsWith("@")) {
                ChatUtil.sendBroadcastMessage("&8(&f" + ProtocolType.getByProtocolID(player.getSession().getProtocolID()).getPrefix() + "&8) &8[" + player.getAccount().getGroup().getPrefix() + "&8] " + player.getThemeType().getColor(1) + player.getUsername() + " &8>> &7" + message.substring(1), false);
                return;
            }
        }

        if (player.isConnected() && !player.isFreecam()) {
            if (player.isOptionEnabled("hidePlayers") && player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol() &&
                packet instanceof CustomPacket && ((CustomPacket) packet).getCustomPacketID() == 0x02) {
                PacketBuffer buffer = ((CustomPacket) packet).getPacketBuffer();

                int targetID = buffer.readVarInt(); //target entity id
                int type = buffer.readVarInt(); //action type

                if (type == 0) {
                    ChatUtil.sendChatMessage("hide " + targetID, player, false);
                    player.getSession().sendPacket(new ServerDestroyEntitiesPacket(targetID));
                }
            }

            if (player.isMother()) {
                player.getBots().forEach(bot -> bot.getSession().fastSendPacket(packet));
            }

            if(player.isOptionEnabled("recordingMacro")) {
                if(player.getMacros().size() > 0) {
                    Macro macro = player.getMacros().get(player.getMacros().size() - 1);
                    PacketUtil.sendTitle(player, player.getThemeType().getColor(1) + "[MACRO id: " + player.getThemeType().getColor(2) +  macro.getId() + player.getThemeType().getColor(1) + "]", "&7packets: " + player.getThemeType().getColor(2) + player.getMacros().get(player.getMacros().size() - 1).getPackets().size(), 0, 10, 0);
                    macro.getPackets().add(packet);
                }
            }

            if (!(packet instanceof ClientKeepAlivePacket)) {
                player.getRemoteSession().sendPacket(packet);
            }
        }
    }
}