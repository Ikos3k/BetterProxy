package me.ANONIMUS.proxy.objects;

import lombok.Data;
import me.ANONIMUS.proxy.protocol.packet.Packet;

import java.util.ArrayList;
import java.util.List;

@Data
public class Macro {
    private final int id;
    private List<Packet> packets = new ArrayList<>();
}
