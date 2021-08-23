package me.ANONIMUS.proxy.managers;

import lombok.Getter;
import me.ANONIMUS.proxy.objects.Config;
import me.ANONIMUS.proxy.protocol.data.DebugType;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Getter
public class ConfigManager {
    private final Config config;
    private final File file;

    public ConfigManager(File file) {
        this.config = new Config();
        this.file = file;

        if(!file.exists()) {
            write();
        }

        read();
    }

    public void write() {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("port", config.port);

        JSONObject motdObj = new JSONObject();
        motdObj.put("versionInfo", config.versionInfo);
        motdObj.put("line1", config.line1);
        motdObj.put("line2", config.line2);

        JSONArray playersArray = new JSONArray();
        playersArray.addAll(config.playerList);

        motdObj.put("playerList", playersArray);

        motdObj.put("icon", config.icon);
        motdObj.put("protocol", config.protocol);

        jsonObj.put("motd", motdObj);

        JSONObject debug = new JSONObject();
        debug.put("debugType", config.debugType.name());
        debug.put("showCustomPackets", config.showCustomPackets);
        debug.put("debugger", config.debugger);

        jsonObj.put("debug", debug);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(jsonObj));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
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

            JSONObject debug = (JSONObject) jsonObj.get("debug");
            config.debugType = DebugType.valueOf(((String) debug.get("debugType")).toUpperCase());
            config.showCustomPackets = (boolean) debug.get("showCustomPackets");
            config.debugger = (boolean) debug.get("debugger");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}