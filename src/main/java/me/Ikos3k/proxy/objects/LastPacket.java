package me.Ikos3k.proxy.objects;

import lombok.Data;
import me.Ikos3k.proxy.protocol.packet.Packet;

@Data
public class LastPacket {
    private Packet lastSentPacket;
    private long sent;
    private Packet lastReceivedPacket;
    private long received;

    public void setSentValue(Packet packet) {
        this.lastSentPacket = packet;
        this.sent = System.currentTimeMillis();
    }

    public int getLastMs() {
        int ms = (int) (received - sent);
        return (ms < 0 ? -ms : ms);
    }
}