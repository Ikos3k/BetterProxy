package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.GroupType;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.objects.Exploit;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;

public class FileUtil {
    public static void createMissing() {
        try {
            final File f1 = new File(BetterProxy.getInstance().getDirFolder() + "/accounts.txt");
            final File f2 = new File(BetterProxy.getInstance().getDirFolder() + "/world");
            final File f3 = new File(BetterProxy.getInstance().getDirFolder() + "/exploits");
            final File f4 = new File(BetterProxy.getInstance().getDirFolder() + "/schematics");
            if (!f1.exists()) { f1.createNewFile(); }
            if (!f2.exists()) { f2.mkdir(); }
            if (!f3.exists()) { f3.mkdir(); }
            if (!f3.exists()) { f4.mkdir(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAccounts() {
        try {
            final File f1 = new File(BetterProxy.getInstance().getDirFolder() + "/accounts.txt");
            final Scanner s = new Scanner(f1);
            while (s.hasNext()) {
                final String[] split = s.next().split(":", 3);
                final Account ac = new Account(split[0], split[1], GroupType.valueOf(split[2]));
                BetterProxy.getInstance().getAccounts().add(ac);
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadExploits() {
        final File directory = new File(BetterProxy.getInstance().getDirFolder() + "/exploits/");
        if (directory.exists()) {
            for (File f : Objects.requireNonNull(directory.listFiles())) {
                if (!f.isDirectory()) {
                    JSONParser parser = new JSONParser();
                    Object obj = null;
                    try {
                        obj = parser.parse(new FileReader(f));
                    } catch (IOException | ParseException ignored) {
                    }
                    JSONObject jsonObj = (JSONObject) obj;
                    int id = ((Long) jsonObj.get("id")).intValue();
                    List<Long> s = ((JSONArray) jsonObj.get("data"));
                    byte[] data = new byte[s.size()];
                    for (int i = 0; i < s.size(); i++) {
                        data[i] = s.get(i).byteValue();
                    }
                    Packet p = new CustomPacket(id, data);

                    BetterProxy.getInstance().getExploitManager().addExploit(new Exploit(f.getName().substring(0, f.getName().length() - 5)) {

                        @Override
                        public void execute(Player sender, int amount) {
                            ChatUtil.sendChatMessage("&2>> &8Crashing started, method: &6" + getName().toUpperCase(), sender, false);
                            final int time = (int) System.currentTimeMillis();
                            sender.getBots().forEach(b -> IntStream.range(0, amount).forEach(i -> b.getSession().sendPacket(p)));
                            final int time2 = (int) System.currentTimeMillis() - time;
                            ChatUtil.sendChatMessage("&2>> &8Crashing complete &7(&e" + time2 + "ms&7)", sender, false);
                        }
                    });
                }
            }
        }
    }
}