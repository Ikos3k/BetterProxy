package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
public class ServerDestroyEntitiesPacket extends Packet {
    private int[] entityIDs;

    public ServerDestroyEntitiesPacket(int... entityIDsIn) {
        this.entityIDs = entityIDsIn;
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.entityIDs = new int[in.readVarInt()];

        for (int i = 0; i < this.entityIDs.length; ++i) {
            this.entityIDs[i] = in.readVarInt();
        }
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.entityIDs.length);

        for (int entityID : this.entityIDs) {
            out.writeVarInt(entityID);
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x13, 47));
    }
}
