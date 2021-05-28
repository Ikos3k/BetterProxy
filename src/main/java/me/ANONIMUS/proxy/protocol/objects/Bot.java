package me.ANONIMUS.proxy.protocol.objects;

import lombok.Data;

@Data
public class Bot {
    private final Player owner;
    private Session session;

    public String getUsername() {
        return session.getUsername();
    }
}