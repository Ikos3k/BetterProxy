package me.ANONIMUS.proxy.protocol.packet.impl.server.status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.status.PlayerInfo;
import me.ANONIMUS.proxy.protocol.data.status.ServerStatusInfo;
import me.ANONIMUS.proxy.protocol.data.status.VersionInfo;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ServerStatusResponsePacket extends Packet {
    private ServerStatusInfo info;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        JsonObject obj = new JsonObject();
        JsonObject ver = new JsonObject();
        ver.addProperty("name", this.info.getVersionInfo().getVersionName());
        ver.addProperty("protocol", this.info.getVersionInfo().getProtocolVersion());
        JsonObject plrs = new JsonObject();
        plrs.addProperty("max", this.info.getPlayerInfo().getMaxPlayers());
        plrs.addProperty("online", this.info.getPlayerInfo().getOnlinePlayers());
        if (this.info.getPlayerInfo().getPlayers().length > 0) {
            JsonArray array = new JsonArray();
            for (GameProfile profile : this.info.getPlayerInfo().getPlayers()) {
                JsonObject o = new JsonObject();
                o.addProperty("name", profile.getName());
                o.addProperty("id", profile.getIdAsString());
                array.add(o);
            }

            plrs.add("sample", array);
        }

        obj.add("version", ver);
        obj.add("players", plrs);
        obj.add("description", new Gson().fromJson(ComponentSerializer.toString(this.info.getDescription()), JsonElement.class));
        if (this.info.getIcon() != null) {
            obj.addProperty("favicon", this.info.getIcon());
        }

        out.writeString(obj.toString());
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        JsonObject obj = new Gson().fromJson(in.readString(), JsonObject.class);
        JsonObject ver = obj.get("version").getAsJsonObject();
        VersionInfo version = new VersionInfo(ver.get("name").getAsString(), ver.get("protocol").getAsInt());
        JsonObject plrs = obj.get("players").getAsJsonObject();
        GameProfile[] profiles = new GameProfile[0];
        if (plrs.has("sample")) {
            JsonArray prof = plrs.get("sample").getAsJsonArray();
            if (prof.size() > 0) {
                profiles = new GameProfile[prof.size()];
                for (int index = 0; index < prof.size(); index++) {
                    JsonObject o = prof.get(index).getAsJsonObject();
                    profiles[index] = new GameProfile(o.get("id").getAsString(), o.get("name").getAsString());
                }
            }
        }

        PlayerInfo players = new PlayerInfo(plrs.get("online").getAsInt(), plrs.get("max").getAsInt(), profiles);
        JsonElement desc = obj.get("description");
        BaseComponent[] description = ComponentSerializer.parse(desc.toString());
        String icon = null;
        if (obj.has("favicon")) {
            icon = obj.get("favicon").getAsString();
        }

        this.info = new ServerStatusInfo(version, players, description, icon);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x00, 47), new Protocol(0x00, 110), new Protocol(0x00, 340));
    }
}