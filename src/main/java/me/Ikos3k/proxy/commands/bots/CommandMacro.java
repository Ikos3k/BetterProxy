package me.Ikos3k.proxy.commands.bots;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.objects.Macro;
import me.Ikos3k.proxy.protocol.objects.Bot;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.impl.client.play.ClientPlayerPositionPacket;
import me.Ikos3k.proxy.utils.ChatUtil;
import me.Ikos3k.proxy.utils.PacketUtil;

public class CommandMacro extends Command {
    public CommandMacro() {
        super("macro", null, null,"[record/play/show/trace/clear] [id]", ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        if(args[1].equalsIgnoreCase("record") || args[1].equalsIgnoreCase("rec")) {
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

            if (macroID <= 0) {
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
        } else if (args[1].equalsIgnoreCase("show")) {
            int macroID = Integer.parseInt(args[2]);

            sender.getMacros().get(macroID - 1).getPackets().stream()
                    .filter(packet -> packet instanceof ClientPlayerPositionPacket)
                    .forEach(packet -> PacketUtil.spawnParticle(sender.getSession(), 21, true, ((ClientPlayerPositionPacket) packet).getPos(), 0, 0, 0, 0, 5));
        } else if (args[1].equalsIgnoreCase("trace")) {
            int macroID = Integer.parseInt(args[2]);

            if(macroID <= 0 || macroID > sender.getMacros().size()) {
                ChatUtil.sendChatMessage("&cunknown macro id!", sender, false);
                return;
            }

            if(sender.getTraceMacro().contains(macroID)) {
                ChatUtil.sendChatMessage("disabled", sender, false);
                sender.getTraceMacro().remove((Object)macroID);
            } else {
                sender.getTraceMacro().add(macroID);
                ChatUtil.sendChatMessage("enabled " + macroID, sender, false);

                if(!sender.getTraceMacro().isEmpty() && !sender.getMacros().isEmpty()) {
                    int[] particles = new int[] { 21, 27, 31, 34, 4, 0, 18, 24 };

                    new Thread(() -> {
                        while (sender.isConnected() && !sender.getTraceMacro().isEmpty()) {
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            for (int i : sender.getTraceMacro()) {
                                sender.getMacros().get(i - 1).getPackets().stream()
                                    .filter(packet -> packet instanceof ClientPlayerPositionPacket)
                                    .forEach(packet -> PacketUtil.spawnParticle(sender.getSession(), particles[i - 1], true, ((ClientPlayerPositionPacket) packet).getPos(), 0, 0, 0, 0, 1));
                            }
                        }
                    }).start();
                }
            }
        } else if (args[1].equalsIgnoreCase("clear")) {
            sender.getMacros().clear();
            sender.getTraceMacro().clear();
        }
    }
}