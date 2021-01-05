package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@NoArgsConstructor
public class ServerPlayerListHeaderFooter extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x47, 47));
        this.getProtocolList().add(new Protocol(0x47, 110));
        this.getProtocolList().add(new Protocol(0x4A, 340));
    }

    private Component header, footer;

    public ServerPlayerListHeaderFooter(Component header, Component footer) {
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(GsonComponentSerializer.gson().serialize(this.header));
        out.writeString(GsonComponentSerializer.gson().serialize(this.footer));
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.header = GsonComponentSerializer.gson().deserialize(in.readString());
        this.footer = GsonComponentSerializer.gson().deserialize(in.readString());
    }
}
