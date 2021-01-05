package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

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
public class ClientCreativeInventoryAction extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x10, 47));
        this.getProtocolList().add(new Protocol(0x18, 110));
        this.getProtocolList().add(new Protocol(0x1B, 340));
    }

    private int slot;
    private ItemStack itemStack;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeShort(slot);
        out.writeItemStackToBuffer(itemStack);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.slot = in.readShort();
        this.itemStack = in.readItemStackFromBuffer();
    }
}