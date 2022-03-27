package me.Ikos3k.proxy.commands.admins;

import me.Ikos3k.proxy.BetterProxy;
import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.impl.CustomPacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class CommandListenChunks extends Command {
    public CommandListenChunks() {
        super("listenchunks", "chunks", ":D", "[listen/save]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        if (args[1].equals("listen")) {
            sender.setListenChunks(!sender.isListenChunks());
            ChatUtil.sendChatMessage("listening chunks: " + sender.getThemeType().getColor(1) + sender.isListenChunks(), sender, false);
        } else if (args[1].equals("save")) {
            sender.setListenChunks(false);

            if (sender.getListenedChunks().isEmpty()) {
                ChatUtil.sendChatMessage("&cThe list is empty", sender, false);
                return;
            }

            int i = 0;
            for (Packet p : sender.getListenedChunks()) {
                FileUtils.writeByteArrayToFile(new File(BetterProxy.getInstance().getDirFolder() + "/world/" + (sender.getSession().getProtocolID() == 47 ? "47" : "other") + "/world_" + i), ((CustomPacket) p).getCustomData());
                i++;
            }
            ChatUtil.sendChatMessage("Successfully saved " + sender.getThemeType().getColor(1) + sender.getListenedChunks().size() + " &7chunks", sender, false);
            sender.getListenedChunks().clear();
        }
    }
}