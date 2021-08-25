package me.ANONIMUS.proxy.utils;

import lombok.SneakyThrows;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

public class FileUtil {
    private static final File accountsFile = new File(BetterProxy.getInstance().getDirFolder(), "accounts.txt");
    private static final File exploitsDirectory = new File(BetterProxy.getInstance().getDirFolder(), "/exploits/");

    public static void createMissing() {
        String[] directories = new String[] { "world", "exploits", "schematics", "players" };

        try {
            for(String d : directories) { new File(BetterProxy.getInstance().getDirFolder() + "/" + d).mkdir(); }

            if(!accountsFile.exists()) {
                FileWriter fileWriter = new FileWriter(accountsFile);
                fileWriter.write("Ikos3k:password:ROOT");
                fileWriter.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAccounts() {
        try {
            Scanner s = new Scanner(accountsFile);
            while (s.hasNext()) {
                String[] split = s.next().split(":", 3);
                BetterProxy.getInstance().getAccounts().put(split[0], new Account(split[1], GroupType.valueOf(split[2])));
            }
            s.close();
        } catch (Exception e) {
            accountsFile.delete();
            System.exit(0);
        }
    }

    public static String getIconFile(String s) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(BetterProxy.getInstance().getDirFolder(), s));
            if (bufferedImage.getWidth() != 64 || bufferedImage.getHeight() != 64) {
                throw new IllegalStateException("> Icon must be 64 pixels wide and 64 pixels high");
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", Base64.getEncoder().wrap(os));

            return "data:image/png;base64," + os.toString(StandardCharsets.ISO_8859_1.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SneakyThrows
    public static void loadExploits() {
        if (exploitsDirectory.listFiles() == null || exploitsDirectory.listFiles().length == 0) {
            int id = 1; String message = "xd";
            byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
            List<Integer> arrayList = new ArrayList<>();
            arrayList.add(msgBytes.length);
            for (int msgByte : msgBytes) { arrayList.add(msgByte); }

            addExploit("example", id, arrayList, false);
            return;
        }

        for (File f : Objects.requireNonNull(exploitsDirectory.listFiles())) {
            if (!f.isDirectory()) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(f));
                JSONObject jsonObj = (JSONObject) obj;
                int id = ((Number) jsonObj.get("id")).intValue();
                JSONArray s = ((JSONArray) jsonObj.get("data"));
                byte[] data = new byte[s.size()];
                for (int i = 0; i < s.size(); i++) {
                    data[i] = ((Number)s.get(i)).byteValue();
                }

                String name = f.getName().substring(0, f.getName().length() - 5);

                boolean compressed = jsonObj.get("compressed") != null && ((Boolean) jsonObj.get("compressed"));

                if(jsonObj.get("compressed") == null) {
                    if(ArrayUtil.getCompressSizeDifference(data) > 0) {
                        addExploit(name, id, ArrayUtil.toList(ArrayUtil.compress(data)), true);
                    } else {
                        addExploit(name, id, s, false);
                    }
                }


                BetterProxy.getInstance().getExploitManager().elements.add(new Exploit(name, "[amount]", compressed) {
                    @SneakyThrows
                    @Override
                    public Packet initPacket(Object... objects) {
                        if(compressed) {
                            return new CustomPacket(id, ArrayUtil.decompress(data));
                        }
                        return new CustomPacket(id, data);
                    }

                    @Override
                    public void execute(Player sender, Object... objects) {
                        ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Crashing started, method: " + sender.getThemeType().getColor(1) + getName().toUpperCase(), sender, false);
                        int time = (int) System.currentTimeMillis();
                        Packet p = initPacket();
                        int amount = Integer.parseInt((String) objects[0]);
                        IntStream.range(0, amount).forEach(i -> sender.getRemoteSession().sendPacket(p));
                        int time2 = (int) System.currentTimeMillis() - time;
                        ChatUtil.sendChatMessage(sender.getThemeType().getColor(1) + ">> &8Crashing complete &7(" + sender.getThemeType().getColor(2) + time2 + "ms&7)", sender, false);
                    }
                });
            }
        }
    }

    private static void addExploit(String name, int id, List<Integer> bytes, boolean compressed) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", id);
        jsonObj.put("compressed", compressed);
        jsonObj.put("data", bytes);

        try (FileWriter fileWriter = new FileWriter(new File(exploitsDirectory, name + ".json"))) {
            fileWriter.write(new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(jsonObj));
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}