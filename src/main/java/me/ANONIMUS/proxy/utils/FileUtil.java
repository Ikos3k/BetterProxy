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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;

public class FileUtil {
    public static void createMissing() {
        final String[] directories = new String[]{"world", "exploits", "schematics", "players"};

        try {
            for (String d : directories) {
                new File(BetterProxy.getInstance().getDirFolder() + "/" + d).mkdir();
            }

            new File(BetterProxy.getInstance().getDirFolder() + "/accounts.txt").createNewFile();
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

    public static String loadIconFile(final String icon) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(BetterProxy.getInstance().getDirFolder(), icon));
            if (bufferedImage.getWidth() != 64 || bufferedImage.getHeight() != 64) {
                throw new IllegalStateException("> Icon must be 64 pixels wide and 64 pixels high");
            }

            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", Base64.getEncoder().wrap(os));

            return "data:image/png;base64," + os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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