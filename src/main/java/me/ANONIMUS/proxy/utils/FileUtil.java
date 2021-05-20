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
            new File(BetterProxy.getInstance().getDirFolder() + "/accounts.txt").mkdir();
            new File(BetterProxy.getInstance().getDirFolder() + "/world").mkdir();
            new File(BetterProxy.getInstance().getDirFolder() + "/exploits").mkdir();
            new File(BetterProxy.getInstance().getDirFolder() + "/schematics").mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAccounts() {
        try {
            final Scanner s = new Scanner(new File(BetterProxy.getInstance().getDirFolder() + "/accounts.txt"));
            while (s.hasNext()) {
                final String[] split = s.next().split(":", 3);
                BetterProxy.getInstance().getAccounts().add(new Account(split[0], split[1], GroupType.valueOf(split[2])));
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

                    BetterProxy.getInstance().getExploitManager().addExploit(new Exploit(f.getName().substring(0, f.getName().length() - 5), "[amount]") {

                        @Override
                        public void execute(Player sender, Object... objects) {
                            ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Crashing started, method: " + sender.getThemeType().getColor(1) + getName().toUpperCase(), sender, false);
                            final int time = (int) System.currentTimeMillis();
                            IntStream.range(0, Integer.parseInt((String) objects[0])).forEach(i -> sender.getRemoteSession().sendPacket(p));
                            final int time2 = (int) System.currentTimeMillis() - time;
                            ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Crashing complete &7(" + sender.getThemeType().getColor(2) + time2 + "ms&7)", sender, false);
                        }
                    });
                }
            }
        }
    }
}