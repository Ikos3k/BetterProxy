package me.Ikos3k.proxy.managers;

import lombok.Getter;
import me.Ikos3k.proxy.objects.Manager;
import me.Ikos3k.proxy.objects.Option;
import me.Ikos3k.proxy.protocol.objects.Player;

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