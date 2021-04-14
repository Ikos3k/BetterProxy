package me.ANONIMUS.proxy.protocol;

import lombok.Data;

@Data
public class Protocol {
    private final int id;
    private final int[] protocols;

    public Protocol(int id, int... protocols) {
        this.id = id;
        this.protocols = protocols;
    }
}