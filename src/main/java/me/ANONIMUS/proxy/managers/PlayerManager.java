package me.ANONIMUS.proxy.managers;

import io.netty.channel.Channel;
import lombok.Getter;
import me.ANONIMUS.proxy.objects.Manager;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;

@Getter
public class PlayerManager extends Manager<Player> {
    public void createPlayer(Session session) {
        elements.add(new Player(session));
    }

    public void removePlayer(Player player) {
        elements.remove(player);
    }

    public Player getPlayer(String name) {
        for (Player p : elements) {
            if (p.getUsername().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Player getPlayer(Channel channel) {
        for (Player p : elements) {
            if (p.getSession().getChannel() == channel) {
                return p;
            }
        }
        return null;
    }
}