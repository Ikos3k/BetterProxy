package me.ANONIMUS.proxy.command;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.protocol.objects.Player;

import java.util.Comparator;

@RequiredArgsConstructor
@Data
public abstract class Command implements Comparator<String> {
    public final String prefix;
    private final String alias;
    private final String desc;
    public final String usage;
    private final CommandType commandType;
    private final ConnectedType connected;

    public abstract void onCommand(final Player sender, final String cmd, final String[] args) throws Exception;

    public int compare(String s1, String s2) {
        int dist1 = Math.abs(s1.length() - prefix.length());
        int dist2 = Math.abs(s2.length() - prefix.length());

        return dist1 - dist2;
    }
}
