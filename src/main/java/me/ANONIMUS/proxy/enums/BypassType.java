package me.ANONIMUS.proxy.enums;

public enum BypassType {
    MSPACK("MC|SMKrzak", "SMKrzak");

    private final String channel;
    private final byte[] data;

    BypassType(String channel, byte[] data) {
        this.channel = channel;
        this.data = data;
    }

    BypassType(String channel, String data) {
        this(channel, data.getBytes());
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getData() {
        return data;
    }
}