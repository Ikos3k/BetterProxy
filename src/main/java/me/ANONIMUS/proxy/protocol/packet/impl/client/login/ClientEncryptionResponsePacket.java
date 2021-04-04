package me.ANONIMUS.proxy.protocol.packet.impl.client.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientEncryptionResponsePacket extends Packet {
    private byte[] sharedSecret;
    private byte[] verifyToken;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeArray(sharedSecret);
        out.writeArray(verifyToken);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        sharedSecret = in.readArray(128);
        verifyToken = in.readArray(128);
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x01, 47));
    }
}