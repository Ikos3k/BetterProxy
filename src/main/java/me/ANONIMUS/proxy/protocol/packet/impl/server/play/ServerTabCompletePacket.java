package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;

@NoArgsConstructor
public class ServerTabCompletePacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(58, 47));
        this.getProtocolList().add(new Protocol(58, 110));
        this.getProtocolList().add(new Protocol(0x0E, 340));
    }

    private String[] matches;

    public ServerTabCompletePacket(final String[] matches) {
        this.matches = matches;
    }

    public String[] getMatches() {
        return this.matches;
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(this.matches.length);
        for (final String match : this.matches) {
            out.writeString(match);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.matches = new String[in.readVarInt()];
        for (int index = 0; index < this.matches.length; ++index) {
            this.matches[index] = in.readString();
        }
    }

}
