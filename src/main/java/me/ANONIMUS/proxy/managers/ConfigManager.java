package me.ANONIMUS.proxy.managers;

import lombok.Getter;
import lombok.SneakyThrows;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.objects.Config;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Getter
public class ConfigManager {
    private final Config config;

    public ConfigManager(File file) {
        this.config = new Config();
        read(file);
        readIconImage();
    }

    private BufferedImage bufferedImage = null;

    public void read(File file) {
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

    @SneakyThrows
    public void readIconImage() {
        File iconFile = new File(BetterProxy.getInstance().getDirFolder(), config.icon);
        if (!iconFile.exists()) return;

        bufferedImage = ImageIO.read(iconFile);

        if (bufferedImage.getWidth() != 64 || bufferedImage.getHeight() != 64) {
            throw new IllegalStateException("> Icon must be 64 pixels wide and 64 pixels high");
        }
    }

}