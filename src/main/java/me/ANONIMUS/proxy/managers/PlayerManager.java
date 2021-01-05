package me.ANONIMUS.proxy.managers;

import io.netty.channel.Channel;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.objects.Session;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerManager {
    private static final List<Player> players = new CopyOnWriteArrayList<>();

    public static List<Player> getPlayers() {
        return players;
    }

    public static void removePlayer(Player player) { players.remove(player); }

    public static void createPlayer(Player player) { players.add(player); }

    public static void createPlayer(Session session) { players.add(new Player(session)); }

    public static Player getPlayer(Session session) {
        for(Player p : players) {
            if(p.getSession() == session) {
                return  p;
            }
        }
        return null;
    }

    public static Player getPlayer(String name) {
        for(Player p : players) {
            if(p.getAccount().getUsername().equals(name)) {
                return  p;
            }
        }
        return null;
    }

    public static Player getPlayer(Channel channel) {
        for(Player p : players) {
            if(p.getSession().getChannel() == channel) {
                return  p;
            }
        }
        return null;
    }
}