package me.ANONIMUS.proxy.objects;

import lombok.Data;

@Data
public class ServerData {
    private final String ip;
    private final String host;
    private final int port;

    public ServerData(String host) {
        this.host = host;
        this.ip = host.split(":")[0];
        this.port = Integer.parseInt(host.split(":")[1]);
    }

    public ServerData(String ip, int port) {
        this.host = ip + ":" + port;
        this.ip = ip;
        this.port = port;
    }
}