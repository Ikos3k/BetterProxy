package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
public class ClientHeldItemChangePacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x09, 47));
        this.getProtocolList().add(new Protocol(0x1A, 340));
    }

    public ClientHeldItemChangePacket(int slotId) {
        this.slotId = slotId;
    }

    private int slotId;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeShort(this.slotId);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.slotId = in.readShort();
    }

    public int getSlotId() {
        return this.slotId;
    }
}