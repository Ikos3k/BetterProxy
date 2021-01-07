package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import io.netty.handler.codec.DecoderException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerCustomPayloadPacket extends Packet {
    private String channel;
    private PacketBuffer data;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.channel);
        out.writeBytes(this.data);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        try {
            this.channel = in.readString(20);
            int i = in.readableBytes();

            if (i >= 0 && i <= 1048576) {
                this.data = new PacketBuffer(in.readBytes(i));
            }
        } catch (DecoderException ignored) {
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x3F, 47), new Protocol(0x3F, 110), new Protocol(0x18, 340));
    }

    public byte[] getByteData() {
        return this.data.readByteArray();
    }
}