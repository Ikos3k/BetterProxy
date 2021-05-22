package me.ANONIMUS.proxy.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import me.ANONIMUS.proxy.protocol.data.Gamemode;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.objects.Session;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerListEntryPacket;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SkinManager {

    @SneakyThrows
    public SkinManager(GameProfile profile, Session session) {
        URL url = new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", profile.getId()));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.connect();
        InputStream inStream = connection.getInputStream();

        String json = new Scanner(inStream, "UTF-8").useDelimiter("\\Z").next();
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        JsonArray properties = object.getAsJsonArray("properties");
        JsonObject jsonObject = properties.get(0).getAsJsonObject();

        profile.getProperties().add(new GameProfile.Property("textures", jsonObject.get("value").getAsString(), jsonObject.get("signature").getAsString()));
        session.sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{new PlayerListEntry(profile, Gamemode.ADVENTURE, 0, profile.getName())}));
    }
}