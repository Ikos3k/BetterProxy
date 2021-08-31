package me.ANONIMUS.proxy.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.ANONIMUS.proxy.objects.Skin;
import me.ANONIMUS.proxy.protocol.data.Gamemode;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerListEntryPacket;
import me.kbrewster.mojangapi.MojangAPI;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class SkinUtil {
    public static PlayerListEntry showSkin(Session session, UUID uuid, Skin skin) {
        if (skin == null) {
            return null;
        }

        GameProfile gameProfile = skin.getGameProfile();
        if (uuid != null) {
            gameProfile.setId(uuid);
        }

        PlayerListEntry playerListEntry = new PlayerListEntry(gameProfile, Gamemode.ADVENTURE, 0, null);

        session.sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{playerListEntry}));

        return playerListEntry;
    }

    public static Skin getSkin(String name) {
        try {
            UUID uuid = MojangAPI.getUUID(name);

            GameProfile gameProfile = new GameProfile(uuid, name);

            URL url = new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", gameProfile.getId()));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();

            String json = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\Z").next();
            JsonObject object = new JsonParser().parse(json).getAsJsonObject();
            JsonArray properties = object.getAsJsonArray("properties");
            JsonObject jsonObject = properties.get(0).getAsJsonObject();

            gameProfile.getProperties().add(new GameProfile.Property("textures", jsonObject.get("value").getAsString(), jsonObject.get("signature").getAsString()));

            return new Skin(gameProfile);
        } catch (Exception e) {
            return null;
        }
    }
}