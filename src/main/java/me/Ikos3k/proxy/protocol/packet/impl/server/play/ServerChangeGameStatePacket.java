package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.Effect;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerChangeGameStatePacket extends Packet {
    private Effect effect;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeByte(effect.getEffectReason());
        out.writeFloat(effect.getValue());
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.effect = new Effect(in.readByte(), in.readFloat());
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x2B, 47), new Protocol(0x1E, 340));
    }
}