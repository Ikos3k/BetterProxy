package me.ANONIMUS.proxy.protocol.packet.impl.client.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.utils.PacketUtil;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientLoginStartPacket extends Packet {
    private String username;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.username);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.username = in.readString();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x00, 47, 109, 110, 210, 340));
    }
}