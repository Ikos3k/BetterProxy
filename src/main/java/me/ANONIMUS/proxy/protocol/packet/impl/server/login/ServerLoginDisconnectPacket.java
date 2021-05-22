package me.ANONIMUS.proxy.protocol.packet.impl.server.login;

import lombok.AllArgsConstructor;
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

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerLoginDisconnectPacket extends Packet {
    private BaseComponent[] reason;

    public ServerLoginDisconnectPacket(String message) {
        this(new ComponentBuilder(message).create());
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(ComponentSerializer.toString(reason));
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.reason = ComponentSerializer.parse(in.readString());
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x00, 47, 107, 108, 109, 110, 210, 315, 335, 338, 340));
    }
}