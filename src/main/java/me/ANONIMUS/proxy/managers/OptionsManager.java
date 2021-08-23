package me.ANONIMUS.proxy.managers;

import lombok.Getter;
import me.ANONIMUS.proxy.objects.Manager;
import me.ANONIMUS.proxy.objects.Option;
import me.ANONIMUS.proxy.protocol.objects.Player;

@Getter
public class OptionsManager extends Manager<Option> {
    public OptionsManager(Player player) {
        elements.add(new Option(player, true, "lastpacket", "last packet :D"));
        elements.add(new Option(player, true, "titlelag", "title lag"));
        elements.add(new Option(player, true, "scoreboard"));
        elements.add(new Option(player, "server tablist", "show server tablist"));
    }

    public Option getOptionByName(String name) {
        return elements.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }
}