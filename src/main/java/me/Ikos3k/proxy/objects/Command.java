package me.Ikos3k.proxy.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.protocol.objects.Player;

@Getter
@RequiredArgsConstructor
public abstract class Command {
    private final String prefix;
    private final String alias;
    private final String desc;
    private final String usage;
    private final ConnectedType connected;

    public abstract void onCommand(final Player sender, final String[] args) throws Exception;
}