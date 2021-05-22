package me.ANONIMUS.proxy.protocol.packet.impl.server.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerLoginSuccessPacket extends Packet {
    private UUID uuid;
    private String username;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.uuid == null ? "" : this.uuid.toString());
        out.writeString(this.username);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.uuid = UUID.fromString(in.readString());
        this.username = in.readString();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x02, 47, 107, 108, 109, 110, 210, 315, 335, 338, 340));
    }
}