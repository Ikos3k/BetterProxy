package me.ANONIMUS.proxy.protocol.packet.impl.server.login;

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
public class ServerLoginEncryptionRequestPacket extends Packet {
    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(serverId);
        out.writeArray(publicKey);
        out.writeArray(verifyToken);
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        serverId = in.readString();
        publicKey = in.readArray();
        verifyToken = in.readArray();
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Collections.singletonList(new Protocol(0x01, 47));
    }
}