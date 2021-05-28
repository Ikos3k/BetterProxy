package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.GroupType;
import me.ANONIMUS.proxy.objects.Account;
import me.ANONIMUS.proxy.objects.Exploit;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

public class FileUtil {
    private final static File accountsFile = new File(BetterProxy.getInstance().getDirFolder(), "accounts.txt");

    public static void createMissing() {
        final String[] directories = new String[] { "world", "exploits", "schematics", "players" };

        try {
            for(String d : directories) {
                new File(BetterProxy.getInstance().getDirFolder() + "/" + d).mkdir();
            }

            if(!accountsFile.exists()) {
                try (FileWriter fileWriter = new FileWriter(accountsFile)) {
                    fileWriter.write("Ikos3k:password:ROOT");
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAccounts() {
        try {
            final Scanner s = new Scanner(accountsFile);
            while (s.hasNext()) {
                final String[] split = s.next().split(":", 3);
                BetterProxy.getInstance().getAccounts().put(split[0], new Account(split[1], GroupType.valueOf(split[2])));
            }
            s.close();
        } catch (Exception e) {
            accountsFile.delete();
            System.exit(0);
        }
    }

    public static String getIconFile(final String s) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(BetterProxy.getInstance().getDirFolder(), s));
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

        if (directory.listFiles() == null || directory.listFiles().length == 0) {
            JSONObject jsonObj = new JSONObject();
            int id = 1; String message = "xd";
            List<Byte> bytesList = new ArrayList<>();
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            bytesList.add((byte) bytes.length);
            for(byte b : bytes) { bytesList.add(b); }

            jsonObj.put("id", id); jsonObj.put("data", bytesList);

            try (FileWriter fileWriter = new FileWriter(new File(directory, "example.json"))) {
                fileWriter.write(new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(jsonObj));
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        for (File f : Objects.requireNonNull(directory.listFiles())) {
            if (!f.isDirectory()) {
                JSONParser parser = new JSONParser();
                Object obj = null;
                try {
                    obj = parser.parse(new FileReader(f));
                } catch (IOException | ParseException ignored) { }
                JSONObject jsonObj = (JSONObject) obj;
                int id = ((Long) jsonObj.get("id")).intValue();
                List<Long> s = ((JSONArray) jsonObj.get("data"));
                byte[] data = new byte[s.size()];
                for (int i = 0; i < s.size(); i++) {
                    data[i] = s.get(i).byteValue();
                }

                Packet p = new CustomPacket(id, data);

                BetterProxy.getInstance().getExploitManager().getExploits().add(new Exploit(f.getName().substring(0, f.getName().length() - 5), "[amount]") {

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