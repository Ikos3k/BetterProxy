package me.Ikos3k.proxy.protocol.packet.impl.client.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntityActionPacket extends Packet {
    private int entityId, actionId, actionParameter;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(entityId);
        out.writeVarInt(actionId);
        out.writeVarInt(actionParameter);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.entityId = in.readVarInt();
        this.actionId = in.readVarInt();
        this.actionParameter = in.readVarInt();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x0B, 47), new Protocol(0x14, 107, 108, 110, 315, 316), new Protocol(0x15, 335, 338, 340));
    }
}