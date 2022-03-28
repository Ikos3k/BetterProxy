package me.Ikos3k.proxy.commands.bots;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.objects.Macro;
import me.Ikos3k.proxy.protocol.objects.Bot;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.utils.ChatUtil;

public class CommandMacro extends Command {
    public CommandMacro() {
        super("macro", null, null,"[record/play/trace] [id]", ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        if(args[1].equalsIgnoreCase("record")) {
            sender.setRecordingMacro(!sender.isRecordingMacro());
            if(sender.isRecordingMacro()) {
                sender.getMacros().add(new Macro(sender.getMacros().size() + 1));
                ChatUtil.sendChatMessage("start recording", sender, false);
            } else {
                ChatUtil.sendChatMessage("end id: " + sender.getMacros().size(), sender, false);
            }
        } else if (args[1].equalsIgnoreCase("play")) {
            ChatUtil.sendChatMessage("playing macro " + args[2], sender, false);
            int macroID = Integer.parseInt(args[2]);

            if(macroID <= 0) {
                ChatUtil.sendChatMessage("unknown macro id!", sender, false);
            }

            Macro macro = sender.getMacros().get(macroID - 1);
            new Thread(() -> {
                for (Bot bot : sender.getBots()) {
                    for (Packet packet : macro.getPackets()) {
                        bot.getSession().sendPacket(packet);
                    }
                }
            }).start();
        } else if (args[1].equalsIgnoreCase("trace")) {
            int macroID = Integer.parseInt(args[2]);

            if(macroID == 0 || macroID < -1 || macroID > sender.getMacros().size()) {
                ChatUtil.sendChatMessage("&cunknown macro id!", sender, false);
                return;
            }

            if(macroID == -1 || sender.getTraceMacro() == macroID) {
                if(sender.getTraceMacro() != -1) {
                    ChatUtil.sendChatMessage("disabled", sender, false);
                    sender.setTraceMacro(-1);
                }
            } else {
                sender.setTraceMacro(macroID);
                ChatUtil.sendChatMessage("enabled " + macroID, sender, false);
            }
        }
    }
}