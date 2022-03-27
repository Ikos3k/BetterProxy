package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.Position;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerUpdateSignPacket extends Packet {
    private Position position;
    private String line1, line2, line3, line4;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writePosition(position);
        out.writeString(line1);
        out.writeString(line2);
        out.writeString(line3);
        out.writeString(line4);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.position = in.readPosition();
        this.line1 = in.readString();
        this.line2 = in.readString();
        this.line3 = in.readString();
        this.line4 = in.readString();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x33, 47), new Protocol(0x1C, 338));
    }
}