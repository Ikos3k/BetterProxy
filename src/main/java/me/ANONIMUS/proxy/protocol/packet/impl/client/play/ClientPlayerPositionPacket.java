package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

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

    private double X;
    private double Y;
    private double Z;

    private boolean ground;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeDouble(this.X);
        out.writeDouble(this.Y);
        out.writeDouble(this.Z);
        out.writeBoolean(this.ground);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.X = in.readDouble();
        this.Y = in.readDouble();
        this.Z = in.readDouble();
        this.ground = in.readBoolean();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x04, 47), new Protocol(0x0C, 107, 108, 109, 110, 210, 316), new Protocol(0x0E, 315, 335), new Protocol(0x0D, 338, 340));
    }
}
