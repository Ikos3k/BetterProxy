package me.ANONIMUS.proxy.protocol.packet.impl.client.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClientLoginStartPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x00, 47));
        this.getProtocolList().add(new Protocol(0x00, 110));
        this.getProtocolList().add(new Protocol(0x00, 340));
    }

    private String username;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.username);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.username = in.readString();
    }
}