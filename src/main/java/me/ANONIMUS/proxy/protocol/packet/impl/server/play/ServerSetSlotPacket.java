package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.ItemStack;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerSetSlotPacket extends Packet {
    private int windowId;
    private int slot;
    private ItemStack item;

    {
        this.getProtocolList().add(new Protocol(0x2F, 47));
        this.getProtocolList().add(new Protocol(0x16, 110));
        this.getProtocolList().add(new Protocol(0x16, 340));
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeByte(this.windowId);
        out.writeShort(this.slot);
        out.writeItemStackToBuffer(this.item);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.windowId = in.readByte();
        this.slot = in.readShort();
        this.item = in.readItemStackFromBuffer();
    }
}
