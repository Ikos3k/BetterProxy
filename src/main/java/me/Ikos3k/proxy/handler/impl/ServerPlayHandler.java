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
import me.Ikos3k.proxy.utils.inventory.InventoryUtil;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.Ikos3k.proxy.utils.PacketUtil.PacketBuilder.DataType.*;

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
                            List<ItemStack> items = new ArrayList<>();

                            String[] nicknames = new String[]{"Kola13567", "Szumir", "Nyatix"};

                            for (String nick : nicknames) {
                                items.add(ItemUtil.skull(SkinUtil.getSkin(nick)));
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
                if (window.getWindowId() == -21) {
                    //soon
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
            if (!player.isConnected()) {
                if (((ClientPlayerPositionPacket) packet).getY() < 65) {
                    PacketUtil.lobbyPosTeleport(player);
                }
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
            if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol() &&
                packet instanceof CustomPacket && ((CustomPacket) packet).getCustomPacketID() == 0x02) {
                PacketBuffer empty = PacketUtil.createEmptyPacketBuffer();
                empty.writeBytes(((CustomPacket) packet).getCustomData());

                int targetID = empty.readVarInt(); //target entity id
                int type = empty.readVarInt(); //read action type

                if(type == 0) {
                    ChatUtil.sendChatMessage("hide " + targetID, player, false);
                    player.getSession().sendPacket(new ServerDestroyEntitiesPacket(targetID));
                }
            }

            if (player.isMother()) {
                player.getBots().forEach(bot -> bot.getSession().fastSendPacket(packet));
            }

            if(player.isRecordingMacro()) {
                if(player.getMacros().size() > 0) {
                    player.getMacros().get(player.getMacros().size() - 1).getPackets().add(packet);
                }
            }

            if(player.getTraceMacro() > 0 && player.getMacros().size() > 0) {
//                Macro macro = player.getMacros().get(player.getMacros().size() - 1);
                Macro macro = player.getMacros().get(player.getTraceMacro() - 1);
                for (Packet packet1 : macro.getPackets()) {
                    if(packet1 instanceof ClientPlayerPositionPacket && player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
                        ClientPlayerPositionPacket posPacket = (ClientPlayerPositionPacket) packet1;

                        boolean longDistance = false;

                        int[] particles = new int[] {
                            21, 27, 31, 34, 4, 0, 18, 24
                        };

                        player.getSession().sendPacket(new PacketUtil.PacketBuilder()
                            .add(INT, particles[player.getTraceMacro() - 1])
                            .add(BOOLEAN, longDistance)
                            .add(FLOAT, (float)posPacket.getX())
                            .add(FLOAT, (float)posPacket.getY())
                            .add(FLOAT, (float)posPacket.getZ())
                            .add(FLOAT, 0F)
                            .add(FLOAT, 0F)
                            .add(FLOAT, 0F)
                            .add(FLOAT, 0F)
                            .add(INT, 10)
                        .build(0x2A));
                    }
                }
            }

            if (!(packet instanceof ClientKeepAlivePacket)) {
                player.getRemoteSession().sendPacket(packet);
            }
        }
    }
}