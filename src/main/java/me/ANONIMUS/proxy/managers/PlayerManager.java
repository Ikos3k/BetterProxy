package me.ANONIMUS.proxy.managers;

import io.netty.channel.Channel;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerManager {
    private final List<Player> players = new CopyOnWriteArrayList<>();

    public void createPlayer(Session session) {
        players.add(new Player(session));
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getAccount().getUsername().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Player getPlayer(Channel channel) {
        for (Player p : players) {
            if (p.getSession().getChannel() == channel) {
                return p;
            }
        }
        return null;
    }
}