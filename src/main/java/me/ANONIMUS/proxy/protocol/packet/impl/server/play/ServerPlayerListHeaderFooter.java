package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerPlayerListHeaderFooter extends Packet {
    private BaseComponent[] header, footer;

    public ServerPlayerListHeaderFooter(String header, String footer) {
        this(new ComponentBuilder(header).create(), new ComponentBuilder(footer).create());
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(ComponentSerializer.toString(this.header));
        out.writeString(ComponentSerializer.toString(this.footer));
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.header = ComponentSerializer.parse(in.readString());
        this.footer = ComponentSerializer.parse(in.readString());
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x48, 109), new Protocol(0x47, 47, 110, 210), new Protocol(0x4A, 340));
    }
}