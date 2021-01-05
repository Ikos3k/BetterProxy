package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
public class ServerCustomPayloadPacket extends Packet {
    {
        this.getProtocolList().add(new Protocol(0x3F, 47));
        this.getProtocolList().add(new Protocol(0x3F, 110));
        this.getProtocolList().add(new Protocol(0x18, 340));
    }

    public ServerCustomPayloadPacket(String channelName, PacketBuffer dataIn) {
        this.channel = channelName;
        this.data = dataIn;

        if (dataIn.writerIndex() > 1048576) {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    private String channel;
    private PacketBuffer data;

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.channel);
        out.writeBytes(this.data);
    }

    @Override
    public void read(PacketBuffer in, int protocol) {
        try {
            this.channel = in.readString(20);
            int i = in.readableBytes();

            if (i >= 0 && i <= 1048576) {
                this.data = new PacketBuffer(in.readBytes(i));
            }
        } catch (Exception ignored) { }
    }

    public String getChannelName() {
        return this.channel;
    }

    public PacketBuffer getBufferData() {
        return this.data;
    }

    public byte[] getByteData() {
        return this.data.readByteArray();
    }
}