package me.ANONIMUS.proxy.objects;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.packet.Packet;

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