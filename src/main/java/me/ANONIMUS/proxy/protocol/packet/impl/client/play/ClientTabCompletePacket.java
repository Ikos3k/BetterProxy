package me.ANONIMUS.proxy.protocol.packet.impl.client.play;

import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.Position;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
public class ClientTabCompletePacket extends Packet {
    private String text;
    private Position position;
    private boolean assumeCMD;

    public ClientTabCompletePacket(final String text) {
        this(text, null);
    }

    public ClientTabCompletePacket(final String text, final Position position) {
        this.text = text;
        this.position = position;
    }

    public ClientTabCompletePacket(final String text, final boolean assumeCMD, final Position position) {
        this.text = text;
        this.assumeCMD = assumeCMD;
        this.position = position;
    }

    public String getText() {
        return this.text;
    }

    {
        this.getProtocolList().add(new Protocol(20, 47));
        this.getProtocolList().add(new Protocol(0x01, 110));
        this.getProtocolList().add(new Protocol(0x01, 340));
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(this.text);
        if(protocol >= 110) {
            out.writeBoolean(this.assumeCMD);
        }
        out.writeBoolean(this.position != null);
        if (this.position != null) {
            out.writePosition(position);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.text = in.readString();
        if(protocol >= 110) {
            this.assumeCMD = in.readBoolean();
        }
        this.position = (in.readBoolean() ? in.readPosition() : null);
    }
}
