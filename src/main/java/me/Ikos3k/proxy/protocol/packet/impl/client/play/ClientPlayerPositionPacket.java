package me.Ikos3k.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.Position;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

/**
 * @author nyatix
 * @created 20.05.2021 - 18:48
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientPlayerPositionPacket extends Packet {
    private double x;
    private double y;
    private double z;
    private boolean ground;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeBoolean(this.ground);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.ground = in.readBoolean();
    }

    public Position getPos() {
        return new Position(x, y, z);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x04, 47), new Protocol(0x0C, 107, 108, 109, 110, 210, 316), new Protocol(0x0E, 315, 335), new Protocol(0x0D, 338, 340));
    }
}
