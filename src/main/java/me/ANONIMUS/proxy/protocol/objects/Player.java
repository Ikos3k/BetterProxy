package me.ANONIMUS.proxy.protocol.objects;

import lombok.Data;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.enums.LanguageType;
import me.ANONIMUS.proxy.enums.ThemeType;
import me.ANONIMUS.proxy.enums.TimeType;
import me.ANONIMUS.proxy.managers.OptionsManager;
import me.ANONIMUS.proxy.objects.*;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.utils.SkinUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Player {
    private final Session session;
    private ConnectedType connectedType = ConnectedType.DISCONNECTED;
    private final OptionsManager optionsManager = new OptionsManager(this);
    private final List<PlayerListEntry> tabList = new ArrayList<>();
    private final List<Packet> listenedChunks = new ArrayList<>();
    private LanguageType languageType = LanguageType.ENGLISH;
    private final LastPacket lastPacket = new LastPacket();
    private final List<Bot> bots = new ArrayList<>();
    private List<String> players = new ArrayList<>();
    private ThemeType themeType = ThemeType.DEFAULT;
    private TimeType timeType = TimeType.DEFAULT;
    private boolean listenChunks = false;
    private Session remoteSession;
    private String prefixCMD = "#";
    private ServerData serverData;
    private boolean pluginsState;
    private boolean playersState;
    private int motherDelay = 25;
    private Account account;
    private boolean mother;
    private boolean logged;
    private Skin skin;

    public void createOptionsFile() {
        try {
            File file = new File(BetterProxy.getInstance().getDirFolder() + "/players", getUsername() + ".json");
            if(file.exists()) {
                file.delete();
            }

            file.createNewFile();

            JSONObject jsonObj = new JSONObject();

            JSONObject optionsObj = new JSONObject();
            for(Option option : optionsManager.elements) {
                optionsObj.put(option.getName(), option.isEnabled());
            }

            optionsObj.put("prefixCMD", prefixCMD);
            optionsObj.put("theme", themeType.name());
            optionsObj.put("language", languageType.name());

            jsonObj.put("options", optionsObj);

            JSONObject skinObj = new JSONObject();

            if(skin == null) {
                this.skin = SkinUtil.getSkin(getUsername());
            }

            if(skin != null) {
                skinObj.put("value", skin.getValue());
                skinObj.put("signature", skin.getSignature());
                jsonObj.put("skin", skinObj);
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(jsonObj));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadOptions() {
        File file = new File(BetterProxy.getInstance().getDirFolder() + "/players", getUsername() + ".json");
        if(!file.exists()) {
            createOptionsFile();
        }

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObj = (JSONObject) obj;

            JSONObject optionsObj = (JSONObject) jsonObj.get("options");
            for(Option option : this.optionsManager.elements) {
                option.setEnabled((Boolean) optionsObj.get(option.getName()));
            }
            this.prefixCMD = (String) optionsObj.get("prefixCMD");
            this.themeType = ThemeType.valueOf((String) optionsObj.get("theme"));
            this.languageType = LanguageType.valueOf((String) optionsObj.get("language"));

            JSONObject skinObj = (JSONObject) jsonObj.get("skin");
            if(skinObj != null) {
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), getUsername());
                gameProfile.getProperties().add(new GameProfile.Property("textures", (String) skinObj.get("value"), (String) skinObj.get("signature")));

                this.skin = new Skin(gameProfile);
            } else {
                this.skin = SkinUtil.getSkin(getUsername());
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.session != null) {
            if (this.session.getPacketHandler() != null) {
                this.session.getPacketHandler().disconnect();
                if(this.account != null) {
                    createOptionsFile();
                }
            }
        }
        if(this.remoteSession != null) {
            this.remoteSession.getChannel().close();
            this.connectedType = ConnectedType.DISCONNECTED;
        }
        BetterProxy.getInstance().getPlayerManager().removePlayer(this);
    }

    public String getUsername() {
        return session.getUsername();
    }

    public boolean isConnected() {
        return remoteSession != null && connectedType == ConnectedType.CONNECTED;
    }
}