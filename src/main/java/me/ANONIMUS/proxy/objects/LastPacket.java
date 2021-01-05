package me.ANONIMUS.proxy.objects;

import lombok.Data;

@Data
public class LastPacket {
    private long sent;
    private long received;
    
    public int getLastMs() {
        int ms = (int) (received - sent);
        if (ms < 0) {
            ms = -ms;
        }
        return ms;
    }
}