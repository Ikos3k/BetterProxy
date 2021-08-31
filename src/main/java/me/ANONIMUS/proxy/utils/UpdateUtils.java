package me.ANONIMUS.proxy.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateUtils {
    public static boolean checkForUpdates(String proxyVersion) {
        try {
            URLConnection connection = new URL("https://raw.githubusercontent.com/Ikos3k/BetterProxy/master/BetterProxy/config.json").openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final JsonReader reader = Json.createReader(in);
            final JsonObject object = reader.readObject();
            reader.close();
            return proxyVersion.equals(object.getString("proxyVersion"));
        } catch (Exception e) {
            return false;
        }
    }
}
