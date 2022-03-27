package me.Ikos3k.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.ItemStack;
import me.Ikos3k.proxy.protocol.data.WindowAction;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientPlayerWindowActionPacket extends Packet {
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
        if (protocol >= 110) {
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
        if (protocol >= 110) {
            this.mode = WindowAction.getById(packetBuffer.readVarInt());
        } else {
            this.mode = WindowAction.getById(packetBuffer.readByte());
        }
        this.item = packetBuffer.readItemStackFromBuffer();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x0E, 47), new Protocol(0x07, 110, 340));
    }
}