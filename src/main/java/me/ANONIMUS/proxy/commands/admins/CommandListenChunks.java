package me.ANONIMUS.proxy.commands.admins;

import me.ANONIMUS.proxy.BetterProxy;
import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.impl.CustomPacket;
import me.ANONIMUS.proxy.utils.ChatUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class CommandListenChunks extends Command {
    public CommandListenChunks() {
        super("listenchunks", "chunks", ":D", "[listen/save]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String cmd, String[] args) throws Exception {
        if(args[1].equals("listen")) {
            sender.setListenChunks(!sender.isListenChunks());
            ChatUtil.sendChatMessage("listening chunks: &6" + sender.isListenChunks(), sender, false);
        } else if (args[1].equals("save")) {
            sender.setListenChunks(false);

            if(sender.getListenedChunks().size() == 0) {
                ChatUtil.sendChatMessage("&cThe list is empty", sender, false);
                return;
            }

            int i = 0;
            for(Packet p : sender.getListenedChunks()) {
                FileUtils.writeByteArrayToFile(new File(BetterProxy.getInstance().getDirFolder() + "/world/" + (sender.getSession().getProtocolID() == 47 ? "47" : "other") + "/world_" + i), ((CustomPacket)p).getCustomData());
                i++;
            }
            ChatUtil.sendChatMessage("Successfully saved &6" + sender.getListenedChunks().size() + " &7chunks", sender, false);
            sender.getListenedChunks().clear();
        }
    }
}