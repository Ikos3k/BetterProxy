package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.Effect;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Collections;
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
        return Collections.singletonList(new Protocol(0x2B, 47));
    }
}