package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.ItemStack;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.List;

@NoArgsConstructor
public class ServerWindowItemsPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x30, 47));
        this.getProtocolList().add(new Protocol(0x14, 340));
    }

    public ServerWindowItemsPacket(int windowIdIn, List<ItemStack> p_i45186_2_) {
        this.windowId = windowIdIn;
        this.itemStacks = new ItemStack[p_i45186_2_.size()];

        for (int i = 0; i < this.itemStacks.length; ++i) {
            ItemStack itemstack = p_i45186_2_.get(i);
            this.itemStacks[i] = itemstack;
        }
    }

    public ServerWindowItemsPacket(int windowIdIn, ItemStack p_i45186_2_) {
        this.windowId = windowIdIn;
        this.itemStacks = new ItemStack[1];

        this.itemStacks[0] = p_i45186_2_;
    }

    private int windowId;
    private ItemStack[] itemStacks;


    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeByte(this.windowId);
        out.writeShort(this.itemStacks.length);

        for (ItemStack itemstack : this.itemStacks) {
            out.writeItemStackToBuffer(itemstack);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.windowId = in.readUnsignedByte();
        int i = in.readShort();
        this.itemStacks = new ItemStack[i];

        for (int j = 0; j < i; ++j) {
            this.itemStacks[j] = in.readItemStackFromBuffer();
        }
    }
}
