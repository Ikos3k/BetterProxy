package me.Ikos3k.proxy.utils;

import io.netty.buffer.Unpooled;
import lombok.Data;
import me.Ikos3k.proxy.protocol.ProtocolType;
import me.Ikos3k.proxy.protocol.data.*;
import me.Ikos3k.proxy.protocol.data.playerlist.PlayerListEntry;
import me.Ikos3k.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.objects.Session;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;
import me.Ikos3k.proxy.protocol.packet.impl.CustomPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class PacketUtil {
    private static final UUID bossBarUUID = UUID.randomUUID();
    private static final int bossBarID = 999;

    private static int spawnEntityID = 1;

    public static PacketBuffer createEmptyPacketBuffer() {
        return new PacketBuffer(Unpooled.buffer());
    }

    public static CustomPacket createCustomPacket(int id, PacketBuilder.CustomData... data) {
        return new PacketBuilder().init(data).build(id);
    }

    public static void fly(Session session, boolean fly) {
        session.sendPacket(new ServerPlayerAbilitiesPacket(false, fly, fly, fly, 0.1f, 0.1f));
    }

    public static void speed(Session session, float speed) {
        session.sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 1.0f, speed));
    }

    public static void lobbyPosTeleport(Player player) {
        player.getSession().sendPacket(new ServerPlayerPosLookPacket(0.5, 70, 0.5, 0.0f, 0.0f));
    }

    public static void clearInventory(Player player) {
        IntStream.range(0, 45).forEach(i -> player.getSession().sendPacket(new ServerSetSlotPacket(0, i, null)));
    }

    public static void clearTabList(Player player) {
        PlayerListEntry[] playerListEntries = new PlayerListEntry[player.getTabList().size()];
        for (int i = 0; i < player.getTabList().size(); i++) {
            playerListEntries[i] = player.getTabList().get(i);
        }
        player.getSession().sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, playerListEntries));
        player.getTabList().clear();
    }

    public static void changeGameMode(Player player, Gamemode gamemode) {
        switch (gamemode) {
            case SURVIVAL:
            case ADVENTURE:
                player.getSession().sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 0, 0));
                break;
            case CREATIVE:
                player.getSession().sendPacket(new ServerPlayerAbilitiesPacket(true, true, true, true, 0.1f, 0.1f));
                break;
            case SPECTATOR:
                player.getSession().sendPacket(new ServerPlayerAbilitiesPacket(true, false, false, false, 0.1f, 0.1f));
                break;
        }
        player.getSession().sendPacket(new ServerChangeGameStatePacket(new Effect(3, gamemode.getId())));
    }

    public static void sendTitle(Player player, String header, String footer) {
        sendTitle(player, header, footer, 10, 10, 10);
    }

    public static void sendTitle(Player player, String header, String footer, int fadeIn, int stay, int fadeOut) {
        if (header != null) {
            player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TITLE, ChatUtil.fixColor(header)));
        }
        if (footer != null) {
            player.getSession().sendPacket(new ServerTitlePacket(TitleAction.SUBTITLE, ChatUtil.fixColor(footer)));
        }
        player.getSession().sendPacket(new ServerTitlePacket(TitleAction.TIMES, fadeIn, stay, fadeOut));
    }

    public static void sendActionBar(String message, Player player) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_12_2.getProtocol()) {
            player.getSession().sendPacket(new ServerTitlePacket(TitleAction.ACTIONBAR, ChatUtil.fixColor(message)));
        } else {
            player.getSession().sendPacket(new ServerChatPacket(ChatUtil.fixColor(message), MessagePosition.HOTBAR));
        }
    }

    public static void sendBoosBar(Player player, String message) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            player.getSession().sendPacket(new ServerSpawnMobPacket(bossBarID, (byte) 63, new Position(0, 0, 0), 0, 0, 0, 0, 0, 0, new EntityMetadata(2, MetadataType.STRING, ChatUtil.fixColor(message)), new EntityMetadata(4, MetadataType.BOOLEAN, true)));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(bossBarUUID, 0, ChatUtil.fixColor(message), 1, 0, 0, (byte) 0));
        }
    }

    public static void clearBossBar(Player player) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            player.getSession().sendPacket(new ServerDestroyEntitiesPacket(bossBarID));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(bossBarUUID, 1));
        }
    }

    public static void destroyAllSpawnedEntities(Session session) {
        int[] ids = new int[spawnEntityID];
        for (int i = 1; i < ids.length; i++) {
            ids[i] = i;
        }

        session.sendPacket(new ServerDestroyEntitiesPacket(ids));
    }

    public static int spawnNextPlayer(Session session, UUID uuid, double x, double y, double z, float yaw, float pitch, int currentItem, EntityMetadata... metadata) {
        session.sendPacket(new ServerSpawnPlayerPacket(spawnEntityID++, uuid, x, y, z, yaw, pitch, currentItem, metadata));

        return spawnEntityID;
    }

    public static int spawnNextEntity(Session session, byte type, Position position, float pitch, float yaw, float headYaw, double motX, double motY, double motZ, EntityMetadata... metadata) {
        session.sendPacket(new ServerSpawnMobPacket(spawnEntityID++, type, position, pitch, yaw, headYaw, motX, motY, motZ, metadata));

        return spawnEntityID;
    }

    @Data
    public static class PacketBuilder {
        private final List<CustomData> data = new ArrayList<>();

        public PacketBuilder add(DataType type, Object value) {
            data.add(new CustomData(type, value));

            return this;
        }

        public PacketBuilder init(CustomData... data) {
            this.data.addAll(Arrays.asList(data));

            return this;
        }

        public CustomPacket build(int id) {
            PacketBuffer buffer = createEmptyPacketBuffer();

            for (CustomData customData : data) {
                switch (customData.getType()) {
                    case VARINT:
                        buffer.writeVarInt((Integer) customData.getValue());
                        break;
                    case INT:
                        buffer.writeInt((Integer) customData.getValue());
                        break;
                    case LONG:
                        buffer.writeLong((Long) customData.getValue());
                        break;
                    case DOUBLE:
                        buffer.writeDouble((Double) customData.getValue());
                        break;
                    case FLOAT:
                        buffer.writeFloat((Float) customData.getValue());
                        break;
                    case BYTE:
                        buffer.writeByte((Byte) customData.getValue());
                        break;
                    case SHORT:
                        buffer.writeShort((Short) customData.getValue());
                        break;
                    case BOOLEAN:
                        buffer.writeBoolean((Boolean) customData.getValue());
                        break;
                    case STRING:
                        buffer.writeString((String) customData.getValue());
                        break;
                    case BYTES:
                        buffer.writeBytes((byte[]) customData.getValue());
                        break;
                }
            }

            return new CustomPacket(id, buffer.readByteArray());
        }

        @Data
        public static class CustomData {
            private final DataType type;
            private final Object value;
        }

        public enum DataType {
            VARINT, INT, LONG, DOUBLE, FLOAT, BYTE, SHORT, BOOLEAN, STRING, BYTES
        }
    }
}