package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.Position;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

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
        return Arrays.asList(new Protocol(0x05, 47), new Protocol(0x43, 109), new Protocol(0x43, 110), new Protocol(0x43, 210), new Protocol(0x46, 340));
    }
}