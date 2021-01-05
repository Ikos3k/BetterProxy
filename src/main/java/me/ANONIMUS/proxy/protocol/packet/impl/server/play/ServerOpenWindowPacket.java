package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.WindowType;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

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

    public ServerOpenWindowPacket(int windowId, WindowType type, String name, int slots, int ownerEntityId) {
        this.windowId = windowId;
        this.type = type;
        this.name = name;
        this.slots = slots;
        this.ownerEntityId = ownerEntityId;
    }

    {
        this.getProtocolList().add(new Protocol(45, 47));
        this.getProtocolList().add(new Protocol(0x13, 340));
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeByte(this.windowId);
        out.writeString(type.getId());
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
}
