package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.ItemStack;
import me.ANONIMUS.proxy.protocol.data.WindowAction;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@Data
@NoArgsConstructor
public class ClientPlayerWindowActionPacket extends Packet {
    {
        this.getProtocolList().add(new Protocol(0x0E, 47));
        this.getProtocolList().add(new Protocol(0x07, 110));
        this.getProtocolList().add(new Protocol(0x07, 340));
    }


    public ClientPlayerWindowActionPacket(int windowId, short slot, int button, WindowAction mode, int action, ItemStack item) {
        this.windowId = windowId;
        this.slot = slot;
        this.button = button;
        this.mode = mode;
        this.action = action;
        this.item = item;
    }
    private int windowId;
    private short slot;
    private int button;
    private WindowAction mode;
    private int action;
    private ItemStack item;

    @Override
    public void write(PacketBuffer packetBuffer, int protocol) throws Exception {
        packetBuffer.writeByte(this.windowId);
        packetBuffer.writeShort(this.slot);
        packetBuffer.writeByte(this.button);
        packetBuffer.writeShort(this.action);
        if(protocol >= 110) {
            packetBuffer.writeVarInt(this.mode.getId());
        } else {
            packetBuffer.writeByte(this.mode.getId());
        }
        packetBuffer.writeItemStackToBuffer(this.item);
    }

    @Override
    public void read(PacketBuffer packetBuffer, int protocol) throws Exception {
        this.windowId = packetBuffer.readByte();
        this.slot = packetBuffer.readShort();
        this.button = packetBuffer.readByte();
        this.action = packetBuffer.readShort();
        if(protocol >= 110) {
            this.mode = WindowAction.getActionById(packetBuffer.readVarInt());
        } else {
            this.mode = WindowAction.getActionById(packetBuffer.readByte());
        }
        this.item = packetBuffer.readItemStackFromBuffer();
    }
}