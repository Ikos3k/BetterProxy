package me.ANONIMUS.proxy.protocol;

import java.util.Arrays;

public enum ProtocolType {

    PROTOCOL_UNKNOWN(0, "UNKNOWN"),
    PROTOCOL_1_8_X(47, "1.8.X"),
    PROTOCOL_1_9(107, "1.9"),
    PROTOCOL_1_9_1(108, "1.9.1"),
    PROTOCOL_1_9_2(109, "1.9.2"),
    PROTOCOL_1_9_4(110, "1.9.4"),
    PROTOCOL_1_10_X(210, "1.10.X"),
    PROTOCOL_1_11(315, "1.11"),
    PROTOCOL_1_11_X(316, "1.11.X"),
    PROTOCOL_1_12(335, "1.12"),
    PROTOCOL_1_12_1(338, "1.12.1"),
    PROTOCOL_1_12_2(340, "1.12.2");

    private final int protocol;
    private final String prefix;

    ProtocolType(int protocol, String prefix) {
        this.protocol = protocol;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getProtocol() {
        return protocol;
    }

    public static ProtocolType getByProtocolID(int protocol) {
        return Arrays.stream(values()).filter(p -> p.protocol == protocol).findFirst().orElse(PROTOCOL_UNKNOWN);
    }
}