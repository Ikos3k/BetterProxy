package me.ANONIMUS.proxy.protocol.objects;

import lombok.Data;
import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.ThemeType;
import me.ANONIMUS.proxy.enums.TimeType;
import me.ANONIMUS.proxy.handler.impl.ServerLoginHandler;
import me.ANONIMUS.proxy.handler.impl.ServerStatusHandler;
import me.ANONIMUS.proxy.managers.OptionsManager;
import me.ANONIMUS.proxy.objects.*;
import me.ANONIMUS.proxy.protocol.data.ConnectionState;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.client.HandshakePacket;
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
    private OptionsManager optionsManager = new OptionsManager();
    private List<PlayerListEntry> tabList = new ArrayList<>();
    private List<Packet> listenedChunks = new ArrayList<>();
    private List<String> players = new ArrayList<>();
    private LastPacket lastPacket = new LastPacket();
    private ThemeType themeType = ThemeType.DEFAULT;
    private TimeType timeType = TimeType.DEFAULT;
    private List<Bot> bots = new ArrayList<>();
    private boolean listenChunks = false;
    private Session remoteSession;
    private String prefixCMD = "#";
    private ServerData serverData;
    private boolean pluginsState;
    private boolean playersState;
    private int motherDelay = 25;
    private boolean connected;
    private Account account;
    private boolean mother;
    private boolean logged;
    private Skin skin;

    public void packetReceived(final Packet packet) {
        if (packet instanceof HandshakePacket) {
            final HandshakePacket handshake = (HandshakePacket) packet;
            session.setProtocolID(handshake.getProtocolId());
            switch (handshake.getNextState()) {
                case 1:
                    session.setConnectionState(ConnectionState.STATUS);
                    session.setPacketHandler(new ServerStatusHandler(this));
                    break;
                case 2:
                    session.setConnectionState(ConnectionState.LOGIN);
                    session.setPacketHandler(new ServerLoginHandler(this));
                    break;
            }
            if (session.getConnectionState() == ConnectionState.HANDSHAKE) {
                session.getChannel().close();
            }
        } else {
            session.getPacketHandler().handlePacket(packet);
        }
    }

    public void createOptionsFile() {
        try {
            File file = new File(BetterProxy.getInstance().getDirFolder() + "/players", account.getUsername() + ".json");
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();

            JSONObject jsonObj = new JSONObject();

            JSONObject optionsObj = new JSONObject();
            for(Option option : optionsManager.getOptions()) {
                optionsObj.put(option.getName(), option.isEnabled());
            }
            optionsObj.put("prefixCMD", prefixCMD);
            optionsObj.put("theme", themeType.name());

            jsonObj.put("options", optionsObj);

            JSONObject skinObj = new JSONObject();

            if(skin == null) {
                this.skin = SkinUtil.getSkin(this.account.getUsername(), null);
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
        File file = new File(BetterProxy.getInstance().getDirFolder() + "/players", account.getUsername() + ".json");
        if(!file.exists()) {
            createOptionsFile();
        }

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObj = (JSONObject) obj;

            JSONObject optionsObj = (JSONObject) jsonObj.get("options");
            for(Option option : this.optionsManager.getOptions()) {
                option.setEnabled(this, (Boolean) optionsObj.get(option.getName()));
            }
            this.prefixCMD = (String) optionsObj.get("prefixCMD");
            this.themeType = ThemeType.valueOf((String) optionsObj.get("theme"));

            JSONObject skinObj = (JSONObject) jsonObj.get("skin");
            if(skinObj != null) {
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), this.account.getUsername());
                gameProfile.getProperties().add(new GameProfile.Property("textures", (String) skinObj.get("value"), (String) skinObj.get("signature")));

                this.skin = new Skin(gameProfile);
            } else {
                this.skin = SkinUtil.getSkin(this.account.getUsername(), null);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        connected = false;
        if (session != null) {
            if (session.getPacketHandler() != null) {
                session.getPacketHandler().disconnected();
                if(account != null) {
                    createOptionsFile();
                }
            }
        }
        if (remoteSession != null) {
            remoteSession.getChannel().close();
        }
        BetterProxy.getInstance().getPlayerManager().removePlayer(this);
    }

    public boolean isConnected() {
        return remoteSession != null && connected;
    }
}