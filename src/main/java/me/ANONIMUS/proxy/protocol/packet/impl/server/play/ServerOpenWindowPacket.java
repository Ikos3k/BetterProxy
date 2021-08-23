package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.WindowType;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerOpenWindowPacket extends Packet {
    private int windowId;
    private WindowType type;
    private String name;
    private int slots;
    private int ownerEntityId;

    public ServerOpenWindowPacket(int windowId, WindowType type, String name, int slots) {
        this(windowId, type, name, slots, 0);
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeByte(this.windowId);
        out.writeString(this.type.getValue());
        out.writeString(this.name);
        out.writeByte(this.slots);
        if (this.type == WindowType.HORSE) {
            out.writeInt(this.ownerEntityId);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.windowId = in.readUnsignedByte();
        this.type = WindowType.getById(in.readString());
        this.name = in.readString();
        this.slots = in.readUnsignedByte();
        if (this.type == WindowType.HORSE) {
            this.ownerEntityId = in.readInt();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(45, 47), new Protocol(0x13, 340));
    }
}