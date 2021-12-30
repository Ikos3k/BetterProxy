package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.*;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.*;

import java.util.UUID;
import java.util.stream.IntStream;

public class PacketUtil {
    private static final UUID bossBarUUID = UUID.randomUUID();

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
            player.getSession().sendPacket(new ServerSpawnMobPacket(1, (byte) 63, new Position(0, 0, 0), 0, 0, 0, 0, 0, 0, new EntityMetadata(2, MetadataType.STRING, ChatUtil.fixColor(message)), new EntityMetadata(4, MetadataType.BOOLEAN, true)));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(bossBarUUID, 0, ChatUtil.fixColor(message), 1, 0, 0, (byte) 0));
        }
    }

    public static void clearBossBar(Player player) {
        if (player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            player.getSession().sendPacket(new ServerDestroyEntitiesPacket(1));
        } else {
            player.getSession().sendPacket(new ServerBossBarPacket(bossBarUUID, 1));
        }
    }
}