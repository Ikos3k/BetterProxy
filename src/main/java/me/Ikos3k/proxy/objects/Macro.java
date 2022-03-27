package me.Ikos3k.proxy.objects;

import lombok.Data;
import me.Ikos3k.proxy.protocol.packet.Packet;

import java.util.ArrayList;
import java.util.List;

@Data
public class Macro {
    private final int id;
    private final List<Packet> packets = new ArrayList<>();
}