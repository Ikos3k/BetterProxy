package me.ANONIMUS.proxy.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigManager {
    private final File file;
    private final Config config;

    public ConfigManager(File f){
        this.file = f;
        this.config = new Config();
    }

    public void read(){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObj = (JSONObject) obj;

            config.port = ((Long) jsonObj.get("port")).intValue();
            JSONObject motd = (JSONObject) jsonObj.get("motd");
            config.versionInfo = (String) motd.get("versionInfo");
            config.line1 = (String) motd.get("line1");
            config.line2 = (String) motd.get("line2");
            JSONArray playerList = (JSONArray) motd.get("playerList");
            config.playerList = new ArrayList<String>(playerList);
            config.icon = (String) motd.get("icon");
            config.protocol = ((Long) motd.get("protocol")).intValue();

            config.debug = (boolean) jsonObj.get("packet_debugger");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return config;
    }
}