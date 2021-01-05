package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ClientChatPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x01, 47));
        this.getProtocolList().add(new Protocol(0x02, 110));
        this.getProtocolList().add(new Protocol(0x02, 340));
    }

    private String message;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        if(protocol >= 110) {
            if (message.length() > 100) {
                message = message.substring(0, 100);
            }
        }
        out.writeString(message);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.message = in.readString(32767);
    }
}
