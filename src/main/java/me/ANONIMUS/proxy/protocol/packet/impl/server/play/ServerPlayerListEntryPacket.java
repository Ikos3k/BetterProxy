package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.Gamemode;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerPlayerListEntryPacket extends Packet {
    private PlayerListEntryAction action;
    private PlayerListEntry[] entries;

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.action = PlayerListEntryAction.getById(in.readVarInt());
        this.entries = new PlayerListEntry[in.readVarInt()];
        for (int count = 0; count < this.entries.length; count++) {
            UUID uuid = in.readUuid();
            GameProfile profile;
            if (this.action == PlayerListEntryAction.ADD_PLAYER) {
                profile = new GameProfile(uuid, in.readString());
            } else {
                profile = new GameProfile(uuid, null);
            }

            PlayerListEntry entry = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    for (int properties = in.readVarInt(), index = 0; index < properties; ++index) {
                        final String propertyName = in.readString();
                        final String value = in.readString();
                        String signature = null;
                        if (in.readBoolean()) {
                            signature = in.readString();
                        }
                        profile.getProperties().add(new GameProfile.Property(propertyName, value, signature));
                    }
                    final Gamemode gameMode = Gamemode.getById(in.readVarInt());
                    final int ping = in.readVarInt();
                    String displayName = null;
                    if (in.readBoolean()) {
                        displayName = in.readString();
                    }
                    entry = new PlayerListEntry(profile, gameMode, ping, displayName);
                    break;
                }
                case UPDATE_GAMEMODE: {
                    final Gamemode mode = Gamemode.getById(in.readVarInt());
                    entry = new PlayerListEntry(profile, mode);
                    break;
                }
                case UPDATE_LATENCY: {
                    int ping = in.readVarInt();

                    entry = new PlayerListEntry(profile, ping);
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    String displayName = null;
                    if (in.readBoolean()) {
                        displayName = in.readString();
                    }

                    entry = new PlayerListEntry(profile, displayName);
                    break;
                }
                case REMOVE_PLAYER:
                    entry = new PlayerListEntry(profile);
                    break;
            }

            this.entries[count] = entry;
        }
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.action.getId());
        out.writeVarInt(this.entries.length);
        for (PlayerListEntry entry : this.entries) {
            out.writeUuid(entry.getProfile().getId());
            switch (this.action) {
                case ADD_PLAYER:
                    out.writeString(entry.getProfile().getName());
                    out.writeVarInt(entry.getProfile().getProperties().size());
                    for (GameProfile.Property property : entry.getProfile().getProperties()) {
                        out.writeString(property.getName());
                        out.writeString(property.getValue());
                        out.writeBoolean(property.hasSignature());
                        if (property.hasSignature()) {
                            out.writeString(property.getSignature());
                        }
                    }

                    out.writeVarInt(entry.getGameMode().getId());
                    out.writeVarInt(entry.getPing());
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        out.writeString(entry.getDisplayName());
                    }

                    break;
                case UPDATE_GAMEMODE:
                    out.writeVarInt(entry.getGameMode().getId());
                    break;
                case UPDATE_LATENCY:
                    out.writeVarInt(entry.getPing());
                    break;
                case UPDATE_DISPLAY_NAME:
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        out.writeString(entry.getDisplayName());
                    }

                    break;
                case REMOVE_PLAYER:
                    break;
            }
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x38, 47), new Protocol(0x2D, 109), new Protocol(0x2D, 110), new Protocol(0x2D, 210), new Protocol(0x2E, 340));
    }
}