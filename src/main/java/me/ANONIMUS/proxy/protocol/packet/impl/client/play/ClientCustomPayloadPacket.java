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
public class ClientCustomPayloadPacket extends Packet {
    private String channel;
    private byte[] data;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(channel);
        out.writeBytes(data);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.channel = in.readString(20);
        data = new byte[in.readableBytes()];
        in.readBytes(data);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x17, 47), new Protocol(0x09, 107, 108, 110, 315, 316, 338, 340), new Protocol(0x0A, 335));
    }
}