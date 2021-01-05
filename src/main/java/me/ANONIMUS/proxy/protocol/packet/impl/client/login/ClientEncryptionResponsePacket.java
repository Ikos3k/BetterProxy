package me.ANONIMUS.proxy.protocol.packet.impl.client.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientEncryptionResponsePacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x01, 47));
    }

    private byte[] sharedSecret;
    private byte[] verifyToken;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeArray( sharedSecret );
        out.writeArray( verifyToken );
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        sharedSecret = in.readArray( 128 );
        verifyToken = in.readArray(  128 );
    }
}