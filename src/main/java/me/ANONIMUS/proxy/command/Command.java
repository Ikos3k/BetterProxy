package me.ANONIMUS.proxy.command;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;

@RequiredArgsConstructor
@Data
public abstract class Command {
    public final String prefix;
    private final String alias;
    private final String desc;
    public final String usage;
    private final CommandType commandType;
    private final ConnectedType connected;

    public abstract void onCommand(final Player sender, final String cmd, final String[] args) throws Exception;
}