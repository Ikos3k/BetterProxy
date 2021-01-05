package me.ANONIMUS.proxy.utils.proxy;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.protocol.data.*;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.*;
import me.AlshainTeam.proxy.protocol.data.*;
import me.AlshainTeam.proxy.protocol.packet.impl.server.play.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class WorldUtil {
    public static void dimSwitch(Player player, ServerJoinGamePacket packet) {
        player.getSession().sendPacket(new ServerRespawnPacket(Dimension.END, Difficulty.PEACEFULL, Gamemode.ADVENTURE, "default_1_1"));
        player.getSession().sendPacket(packet);
        player.getSession().sendPacket(new ServerRespawnPacket(packet.getDimension(), packet.getDifficulty(), packet.getGamemode(), packet.getLevelType()));
    }

    public static void emptyWorld(Player player) {
        dimSwitch(player, new ServerJoinGamePacket(0, Gamemode.SURVIVAL, Dimension.OVERWORLD, Difficulty.PEACEFULL, 1, "default_1_1", false));
        player.getSession().sendPacket(new ServerSpawnPositionPacket(new Position(0, 1, 0)));
        player.getSession().sendPacket(new ServerPlayerPositionRotationPacket(0, 1, 0, 0.0f, 0.0f));
        player.getSession().sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 0f, 0f));
    }

    public static void lobby(Player player, boolean clear) {
        if (clear) { emptyWorld(player); }
        PacketUtil.clearInventory(player);
        PacketUtil.clearTabList(player);
        try {
            int i = 0;
            while (new File(BetterProxy.getInstance().getDirFolder() + "/world/" + (player.getSession().getProtocolID() != 47 ? "other" : "47") + "/world_" + i).exists()) {
                final byte[] data = Files.readAllBytes(new File(BetterProxy.getInstance().getDirFolder() + "/world/" + (player.getSession().getProtocolID() != 47 ? "other" : "47") + "/world_" + i).toPath());
                Packet p = new CustomPacket(player.getSession().getProtocolID() == 47 ? 0x26 : 0x20, data);
                player.getSession().sendPacket(p);
                i++;
            }
        } catch (IOException ignored) { }

        player.getSession().sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 0f, 0f));
        player.getSession().sendPacket(new ServerPlayerPositionRotationPacket(0.5, 70, 0.5, 0.0f, 0.0f));

        if(player.getSession().getProtocolID() == 47) {
            spawnPlayers(player);
            loadTextsOnSign(player);
        }
        ItemUtil.loadStartItems(player);
    }

    private static void spawnPlayers(Player p) {
        PacketUtil.clearTabList(p);

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(new File(BetterProxy.getInstance().getDirFolder() + "/world/players.json")));
            JSONArray jsonObj = (JSONArray) obj;

            int i = 1;
            for (Object players : jsonObj.toArray()) {
                JSONObject player = (JSONObject) ((JSONObject) players).get("player");
                String name = (String) player.get("name");

                JSONObject position = (JSONObject) player.get("position");
                double x = ((Number)position.get("x")).doubleValue();
                double y =((Number)position.get("y")).doubleValue();
                double z = ((Number)position.get("z")).doubleValue();

                JSONObject textures = (JSONObject) player.get("textures");
                String value = (String) textures.get("value");
                String signature = (String) textures.get("signature");

                GameProfile profile = new GameProfile(UUID.randomUUID(), name);
                profile.getProperties().add(new GameProfile.Property("textures", value, signature));
                p.getSession().sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{ new PlayerListEntry(profile, Gamemode.ADVENTURE, 0, name) }));
                p.getSession().sendPacket(new ServerSpawnPlayerPacket(i, profile.getId(), x, y, z, 0, 0, 0, new EntityMetadata[0]));
                i++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void loadTextsOnSign(Player p) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(new File(BetterProxy.getInstance().getDirFolder() + "/world/signs.json")));
            JSONArray jsonObj = (JSONArray) obj;

            jsonObj.forEach(signs -> {
                JSONObject sign = (JSONObject) ((JSONObject) signs).get("sign");
                String text_1 = (String) sign.get("text_1");
                String text_2 = (String) sign.get("text_2");
                String text_3 = (String) sign.get("text_3");
                String text_4 = (String) sign.get("text_4");

                JSONObject position = (JSONObject) sign.get("position");
                long x = ((Number)position.get("x")).longValue();
                long y =((Number)position.get("y")).longValue();
                long z = ((Number)position.get("z")).longValue();
                p.getSession().sendPacket(new ServerUpdateSignPacket(new Position(x, y, z), ChatUtil.fixColor(text_1), ChatUtil.fixColor(text_2), ChatUtil.fixColor(text_3), ChatUtil.fixColor(text_4)));
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}