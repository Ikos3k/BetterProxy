package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerPlayerAbilitiesPacket extends Packet {
    private boolean damage, flying, allowFlying, creative;
    private float flySpeed, walkSpeed;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        byte flags = 0;
        if (this.damage) {
            flags = (byte) (flags | 0x1);
        }
        if (this.flying) {
            flags = (byte) (flags | 0x2);
        }
        if (this.allowFlying) {
            flags = (byte) (flags | 0x4);
        }
        if (this.creative) {
            flags = (byte) (flags | 0x8);
        }
        out.writeByte(flags);
        out.writeFloat(this.flySpeed);
        out.writeFloat(this.walkSpeed);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        final byte flags = in.readByte();
        this.damage = ((flags & 0x1) > 0);
        this.flying = ((flags & 0x2) > 0);
        this.allowFlying = ((flags & 0x4) > 0);
        this.creative = ((flags & 0x8) > 0);
        this.flySpeed = in.readFloat();
        this.walkSpeed = in.readFloat();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x39, 47), new Protocol(0x2B, 109, 110, 210), new Protocol(0x2C, 340));
    }
}