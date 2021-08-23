package me.ANONIMUS.proxy.protocol;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Protocol {
    private final int id;
    private final int[] protocols;

    public Protocol(int id, int... protocols) {
        this.id = id;
        this.protocols = protocols;
    }

    public Protocol(int id, ProtocolType... protocolTypes) {
        this.id = id;

        this.protocols = new int[protocolTypes.length];
        for (int i = 0; i < this.protocols.length; i++) {
            this.protocols[i] = protocolTypes[i].getProtocol();
        }
    }

    public static List<Protocol> getAllSameProtocols(int id) {
        return Collections.singletonList(new Protocol(id, ProtocolType.values()));
    }
}