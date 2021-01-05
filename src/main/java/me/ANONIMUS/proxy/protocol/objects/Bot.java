package me.ANONIMUS.proxy.protocol.objects;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ANONIMUS.proxy.objects.ServerData;

@Data
@RequiredArgsConstructor
public class Bot {
    private Player owner;
    private Session session;
    private String username;
    private ServerData serverData;

    public Bot(String username, Player owner){
        this.username = username;
        this.owner = owner;
    }

    public void disconnected() {
        owner.getBots().remove(this);
    }
}