package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ServerBossBarPacket extends Packet {
    private UUID uuid;
    private int action;
    private BaseComponent[] title;
    private float health;
    private int color;
    private int division;
    private byte flags;

    public ServerBossBarPacket(UUID uuid, int action) {
        this.uuid = uuid;
        this.action = action;
    }

    public ServerBossBarPacket(UUID uuid, int action, String title, float health, int color, int division, byte flags) {
        this.uuid = uuid;
        this.action = action;
        this.title = new ComponentBuilder(title).create();
        this.health = health;
        this.color = color;
        this.division = division;
        this.flags = flags;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeUuid(uuid);
        out.writeVarInt(action);

        switch (action) {
            case 0:
                out.writeString(ComponentSerializer.toString(title));
                out.writeFloat(health);
                out.writeVarInt(color);
                out.writeVarInt(division);
                out.writeByte(flags);
                break;
            case 2:
                out.writeFloat(health);
                break;
            case 3:
                out.writeString(ComponentSerializer.toString(title));
                break;
            case 4:
                out.writeVarInt(color);
                out.writeVarInt(division);
                break;
            case 5:
                out.writeByte(flags);
                break;
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        uuid = in.readUuid();
        action = in.readVarInt();

        switch (action) {
            case 0:
                title = ComponentSerializer.parse(in.readString());
                health = in.readFloat();
                color = in.readVarInt();
                division = in.readVarInt();
                flags = in.readByte();
                break;
            case 2:
                health = in.readFloat();
                break;
            case 3:
                title = ComponentSerializer.parse(in.readString());
                break;
            case 4:
                color = in.readVarInt();
                division = in.readVarInt();
                break;
            case 5:
                flags = in.readByte();
                break;
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x0C, 107, 108, 109, 110, 210, 315, 316, 335, 338, 340));
    }
}