package me.Ikos3k.proxy.utils;

import io.netty.buffer.Unpooled;
import me.Ikos3k.proxy.protocol.ProtocolType;
import me.Ikos3k.proxy.protocol.data.*;
import me.Ikos3k.proxy.protocol.data.playerlist.PlayerListEntry;
import me.Ikos3k.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.objects.Session;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;
import me.Ikos3k.proxy.protocol.packet.impl.CustomPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.*;

import java.util.UUID;
import java.util.stream.IntStream;

import static me.Ikos3k.proxy.protocol.packet.Packet.Builder.DataType.*;

public class PacketUtil {
    private static final UUID bossBarUUID = UUID.randomUUID();
    private static final int bossBarID = 999;

    private static int spawnEntityID = 1;

    public static PacketBuffer createEmptyPacketBuffer() {
        return new PacketBuffer(Unpooled.buffer());
    }

    public static CustomPacket createCustomPacket(int id, Packet.Builder.CustomData... data) {
        return new Packet.Builder().init(data).build(id);
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

    public static void spawnParticle(Session session, int particleID, boolean longDistance, Position pos, float offsetX, float offsetY, float offsetZ, float particleData, int particleCount) {
        session.sendPacket(new Packet.Builder()
            .add(INT, particleID)
            .add(BOOLEAN, longDistance)
            .add(FLOAT, (float) pos.getX())
            .add(FLOAT, (float) pos.getY())
            .add(FLOAT, (float) pos.getZ())
            .add(FLOAT, offsetX)
            .add(FLOAT, offsetY)
            .add(FLOAT, offsetZ)
            .add(FLOAT, particleData)
            .add(INT, particleCount)
        .build(0x2A));
    }
}