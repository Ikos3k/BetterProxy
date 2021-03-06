package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientStatusPacket extends Packet {
    private int actionId;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(actionId);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.actionId = in.readVarInt();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x16, 47), new Protocol(0x03, 340));
    }
}