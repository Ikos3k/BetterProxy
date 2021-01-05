package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerDisconnectPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x40, 47));
        this.getProtocolList().add(new Protocol(0x1A, 110));
        this.getProtocolList().add(new Protocol(0x1A, 340));
    }

    private Component reason;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(GsonComponentSerializer.gson().serialize(reason));
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.reason = GsonComponentSerializer.gson().deserialize(in.readString());
    }
}