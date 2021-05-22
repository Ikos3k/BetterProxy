package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientChatPacket extends Packet {
    private String message;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        if (protocol >= 110 && message.length() > 100) {
            message = message.substring(0, 100);
        }
        out.writeString(message);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.message = in.readString(32767);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x01, 47), new Protocol(0x02, 107, 108, 109, 110, 210, 315, 316, 338, 340), new Protocol(0x03, 335));
    }
}