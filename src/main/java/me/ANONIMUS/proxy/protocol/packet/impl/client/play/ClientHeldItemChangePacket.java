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
@AllArgsConstructor
@NoArgsConstructor
public class ClientHeldItemChangePacket extends Packet {
    private int slotId;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeShort(this.slotId);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.slotId = in.readShort();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x09, 47), new Protocol(0x17, 107, 108, 109, 110, 315, 316), new Protocol(0x1A, 335, 338, 340));
    }
}