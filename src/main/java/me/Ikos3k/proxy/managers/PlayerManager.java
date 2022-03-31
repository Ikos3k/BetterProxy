package me.Ikos3k.proxy.managers;

import io.netty.channel.Channel;
import lombok.Getter;
import me.Ikos3k.proxy.objects.Manager;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.objects.Session;

import java.util.Optional;

@Getter
public class PlayerManager extends Manager<Player> {
    public void createPlayer(Session session) {
        elements.add(new Player(session));
    }

    public void removePlayer(Player player) {
        elements.remove(player);
    }

    public Optional<Player> getPlayer(String name) {
       return elements.stream().filter(p -> p.getUsername().equals(name)).findFirst();
    }

    public Optional<Player> getPlayer(Channel channel) {
        return elements.stream()
                .filter(p -> p.getSession().getChannel() == channel).findFirst();
    }
}