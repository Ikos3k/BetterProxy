package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.Position;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerUpdateBlockEntity extends Packet {
    private Position position;
    private int action;
    private NBTTagCompound nbt;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writePosition(position);
        out.writeByte(action);
        out.writeNBTTagCompoundToBuffer(nbt);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.position = in.readPosition();
        this.action = in.readByte();
        this.nbt = in.readNBTTagCompoundFromBuffer();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x09, 110));
    }
}