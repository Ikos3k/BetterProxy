package me.ANONIMUS.proxy.utils;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.objects.Schematic;
import me.ANONIMUS.proxy.protocol.ProtocolType;
import me.ANONIMUS.proxy.protocol.data.*;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntry;
import me.ANONIMUS.proxy.protocol.data.playerlist.PlayerListEntryAction;
import me.ANONIMUS.proxy.protocol.objects.GameProfile;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.*;
import net.minecraft.nbt.NBTTagCompound;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.UUID;

public class WorldUtil {
    public static void dimSwitch(Player player, ServerJoinGamePacket packet) {
        player.getSession().sendPacket(new ServerRespawnPacket(Dimension.END, Difficulty.PEACEFULL, Gamemode.SURVIVAL, "default_1_1"));
        player.getSession().sendPacket(packet);
        player.getSession().sendPacket(new ServerRespawnPacket(packet.getDimension(), packet.getDifficulty(), packet.getGamemode(), packet.getLevelType()));
    }

    public static void emptyWorld(Player player) {
        dimSwitch(player, new ServerJoinGamePacket(0, Gamemode.SURVIVAL, Dimension.OVERWORLD, Difficulty.PEACEFULL, 1, "default_1_1", false));
        player.getSession().sendPacket(new ServerSpawnPositionPacket(new Position(0, 1, 0)));
        player.getSession().sendPacket(new ServerPlayerAbilitiesPacket(false, true, false, false, 0f, 0f));
        player.getSession().sendPacket(new ServerPlayerPosLookPacket(0, 0, 0, 180, 90));
    }

    public static void lobby(Player player, boolean clear) {
        if (clear) { emptyWorld(player); }
        PacketUtil.clearInventory(player);
        PacketUtil.clearTabList(player);

        if(player.getSession().getProtocolID() != ProtocolType.PROTOCOL_1_9_2.getProtocol()) {
            try {
                int i = 0;
                while (new File(BetterProxy.getInstance().getDirFolder() + "/world/" + (player.getSession().getProtocolID() != 47 ? "other" : "47") + "/world_" + i).exists()) {
                    final byte[] data = Files.readAllBytes(new File(BetterProxy.getInstance().getDirFolder() + "/world/" + (player.getSession().getProtocolID() != 47 ? "other" : "47") + "/world_" + i).toPath());
                    Packet p = new CustomPacket(player.getSession().getProtocolID() == 47 ? 0x26 : 0x20, data);
                    player.getSession().sendPacket(p);
                    i++;
                }
            } catch (IOException ignored) { }
        }

//        try {
//            testLoadSchematic(player, new Schematic(CompressedStreamTools.readCompressed(new FileInputStream(BetterProxy.getInstance().getDirFolder() + "/schematics/example.schematic"))), 0, 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        player.getSession().sendPacket(new ServerPlayerAbilitiesPacket(false, false, false, false, 0f, 0f));
        player.getSession().sendPacket(new ServerPlayerPosLookPacket(0.5, 70, 0.5, 0.0f, 0.0f));

        if(player.getSession().getProtocolID() == ProtocolType.PROTOCOL_1_8_X.getProtocol()) {
            spawnPlayers(player);
            loadTextsOnSign(player);
        }
        ItemUtil.loadStartItems(player);
    }

    private static void testLoadSchematic(final Player player, final Schematic schematic, final int posX, final int posZ) {
        NBTTagCompound nbt = schematic.getNbtTagCompound();
        final int width = nbt.getShort("Width");
        final int height = nbt.getShort("Height");
        final int length = nbt.getShort("Length");
        final byte[] blocksBytes = nbt.getByteArray("Blocks");
        final byte[] dataBytes = nbt.getByteArray("Data");

        Chunk[] chunks = new Chunk[16];

        byte[] biomes = new byte[256];
        Arrays.fill(biomes, (byte) ((Math.sin(posX * posZ) + 1.0D) * 10.0D));

        ShortArray3d blocks = new ShortArray3d(4096);
        NibbleArray3d blockLight = new NibbleArray3d(4096);
        NibbleArray3d skylight = new NibbleArray3d(4096);

        skylight.fill(15);

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < length; z++) {
                for (int y = 0; y < height; y++) {
                    final int blockIndex = y * width * length + z * width + x;
                    final int block = blocksBytes[blockIndex];
                    blocks.setBlockAndData(x, y, z, block, dataBytes[blockIndex]);
                }
            }
        }
        chunks[0] = new Chunk(blocks, blockLight, skylight);
        player.getSession().sendPacket(new ServerChunkDataPacket(posX, posZ, chunks, biomes));
    }

    private static void spawnPlayers(Player p) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(BetterProxy.getInstance().getDirFolder() + "/world/players.json"));
            JSONArray jsonObj = (JSONArray) obj;

            int i = 1;
            for (Object players : jsonObj.toArray()) {
                JSONObject player = (JSONObject) ((JSONObject) players).get("player");
                String name = (String) player.get("name");

                JSONObject position = (JSONObject) player.get("position");
                double x = ((Number)position.get("x")).doubleValue();
                double y = ((Number)position.get("y")).doubleValue();
                double z = ((Number)position.get("z")).doubleValue();

                JSONObject textures = (JSONObject) player.get("textures");
                String value = (String) textures.get("value");
                String signature = (String) textures.get("signature");

                GameProfile profile = new GameProfile(UUID.randomUUID(), name);
                profile.getProperties().add(new GameProfile.Property("textures", value, signature));
                p.getSession().sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{ new PlayerListEntry(profile, Gamemode.ADVENTURE, 0, name) }));
                p.getSession().sendPacket(new ServerSpawnPlayerPacket(i, profile.getId(), x, y, z, 0, 0, 0, new EntityMetadata[] { new EntityMetadata(10, MetadataType.BYTE, Byte.MAX_VALUE) }));
                i++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void loadTextsOnSign(Player p) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(BetterProxy.getInstance().getDirFolder() + "/world/signs.json"));
            JSONArray jsonObj = (JSONArray) obj;

            jsonObj.forEach(signs -> {
                JSONObject sign = (JSONObject) ((JSONObject) signs).get("sign");
                String text_1 = (String) sign.get("text_1");
                String text_2 = (String) sign.get("text_2");
                String text_3 = (String) sign.get("text_3");
                String text_4 = (String) sign.get("text_4");

                JSONObject position = (JSONObject) sign.get("position");
                int x = ((Number)position.get("x")).intValue();
                int y = ((Number)position.get("y")).intValue();
                int z = ((Number)position.get("z")).intValue();
                p.getSession().sendPacket(new ServerUpdateSignPacket(new Position(x, y, z), ChatUtil.fixColor(text_1), ChatUtil.fixColor(text_2), ChatUtil.fixColor(text_3), ChatUtil.fixColor(text_4)));
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}