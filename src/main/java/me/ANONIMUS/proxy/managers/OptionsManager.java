package me.ANONIMUS.proxy.managers;

import me.ANONIMUS.proxy.objects.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionsManager {
    private final List<Option> options = new ArrayList<>();

    public OptionsManager() {
        options.add(new Option(true, "lastpacket", "last packet :D"));
        options.add(new Option(true, "titlelag", "title lag"));
        options.add(new Option(true, "scoreboard"));
    }

    public Option getOptionByName(String name) {
        return options.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Option> getOptions() {
        return options;
    }
}