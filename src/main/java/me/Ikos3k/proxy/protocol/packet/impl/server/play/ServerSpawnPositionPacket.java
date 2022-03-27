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
public class ServerSpawnPositionPacket extends Packet {
    private Position position;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writePosition(this.position);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.position = in.readPosition();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x05, 47), new Protocol(0x43, 107, 108, 109, 110, 210, 315, 316), new Protocol(0x45, 335), new Protocol(0x46, 338, 340));
    }
}