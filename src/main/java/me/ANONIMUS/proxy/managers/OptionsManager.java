package me.ANONIMUS.proxy.managers;

import me.ANONIMUS.proxy.objects.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionsManager {
    private final List<Option> options = new ArrayList<>();

    public OptionsManager() {
        options.add(new Option("test1", "TEST", "TEST"));
        options.add(new Option(true, "test2", ":D"));
        options.add(new Option(true, "test3", "?"));
        options.add(new Option(true, "test4", ";D "));
        options.add(new Option("test5", "test option :p"));
    }

    public Option getOptionByName(String name) {
        return options.stream().filter(s -> s.getName().equals(name.toLowerCase())).findFirst().orElse(null);
    }

    public List<Option> getOptions() {
        return options;
    }
}