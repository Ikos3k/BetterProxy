package me.ANONIMUS.proxy.protocol.packet;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Protocol {
    private final int id, protocol;

    public static List<Protocol> protocols(int id, int... protocols) {
        List<Protocol> protocolList = new ArrayList<>();
        for(int protocol : protocols) {
            protocolList.add(new Protocol(id, protocol));
        }
        return protocolList;
    }
}