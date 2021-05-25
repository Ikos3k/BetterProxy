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
import me.kbrewster.exceptions.APIException;
import me.kbrewster.exceptions.InvalidPlayerException;
import me.kbrewster.mojangapi.MojangAPI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class SkinUtil {
    public static PlayerListEntry showSkin(Session session, UUID uuid, Skin skin) {
        if(skin == null) {
            return null;
        }

        GameProfile gameProfile = skin.getGameProfile();
        if(uuid != null) {
            gameProfile.setId(uuid);
        }

        PlayerListEntry playerListEntry = new PlayerListEntry(gameProfile, Gamemode.ADVENTURE, 0, null);

        session.sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[] { playerListEntry }));

        return playerListEntry;
    }

    public static Skin getSkin(final String name, UUID uuid) {
        try {
            if(uuid == null) {
                try {
                    uuid = MojangAPI.getUUID(name);
                } catch (InvalidPlayerException | APIException ignored) {
                    return null;
                }
            }

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
        } catch (IOException e) {
            return null;
        }
    }
}