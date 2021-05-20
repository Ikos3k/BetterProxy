package me.ANONIMUS.proxy.objects;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.packet.Packet;

@Data
public class LastPacket {
    private long sent;
    private long received;
    private Packet packet;

    public int getLastMs() {
        int ms = (int) (received - sent);
        if (ms < 0) {
            ms = -ms;
        }
        return ms;
    }
}