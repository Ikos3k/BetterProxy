package me.ANONIMUS.proxy.commands.more;

import me.ANONIMUS.proxy.enums.CommandType;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.client.play.ClientTabCompletePacket;
import me.ANONIMUS.proxy.utils.ChatUtil;

import javax.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CommandPlayers extends Command {
    public CommandPlayers() { super("players", null, null, "[tablist/tabcomplete/tabcomplete2/api/clear]", CommandType.MORE, ConnectedType.CONNECTED); }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if (args[1].equalsIgnoreCase("tablist")) {
            sender.getPlayers().clear();
            sender.setPlayersState(false);
            String out = sender.getPlayers().toString();
            if (out.equals("[]")) {
                ChatUtil.sendChatMessage("&cNo players found!", sender, true);
                return;
            }
            out = out.replace("[", "");
            out = out.replace("]", "");
            ChatUtil.sendChatMessage("&f" + out + " &7[&f" + sender.getPlayers().size() + "&7]", sender, true);
        } else if (args[1].equalsIgnoreCase("tabcomplete")) {
            sender.getPlayers().clear();
            sender.setPlayersState(true);
            ChatUtil.sendChatMessage("&7Try to get players! &7(&4TabComplete&7)", sender, true);
            sender.getRemoteSession().sendPacket(new ClientTabCompletePacket("/tpa "));
        } else if (args[1].equalsIgnoreCase("tabcomplete2")) {
            sender.getPlayers().clear();
            sender.setPlayersState(true);
            ChatUtil.sendChatMessage("&7Try to get players! &7(&4TabComplete&7)", sender, true);
            sender.getRemoteSession().sendPacket(new ClientTabCompletePacket("/msg "));
        } else if (args[1].equalsIgnoreCase("api")) {
            sender.getPlayers().clear();
            ChatUtil.sendChatMessage("&7Try to get players! &7(&4API&7)", sender, true);
            String url = "https://api.mcsrvstat.us/2/" + sender.getServerData().getHost();
            final URL website = new URL(url);
            final URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final JsonReader reader = Json.createReader(in);
            final JsonObject object = reader.readObject();
            reader.close();
            JsonArray players = object.getJsonObject("players").getJsonArray("list");
            for (JsonValue p : players) {
                sender.getPlayers().add(p.toString());
            }
            String out = sender.getPlayers().toString();
            if (out.equals("[]")) {
                ChatUtil.sendChatMessage("&cNo players found!", sender, true);
                return;
            }
            out = out.replace("[", "");
            out = out.replace("]", "");
            ChatUtil.sendChatMessage("&f" + out + " &7[&f" + sender.getPlayers().size() + "&7]", sender, true);
        } else if (args[1].equalsIgnoreCase("clear")){
            if (sender.getPlayers().size() == 0) {
                ChatUtil.sendChatMessage("&cThere is no one on the list &c:[", sender, true);
                return;
            }
            ChatUtil.sendChatMessage("&6The list with players was cleared successfully &8(&e" + sender.getPlayers().size() + ")", sender, true);
            sender.getPlayers().clear();
        }
    }
}