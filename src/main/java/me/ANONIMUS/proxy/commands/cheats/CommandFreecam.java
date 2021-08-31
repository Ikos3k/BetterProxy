package me.ANONIMUS.proxy.commands.cheats;

import me.ANONIMUS.proxy.enums.ConnectedType;
import me.ANONIMUS.proxy.objects.Command;
import me.ANONIMUS.proxy.protocol.data.Gamemode;
import me.ANONIMUS.proxy.protocol.objects.Player;
import me.ANONIMUS.proxy.protocol.packet.impl.server.play.ServerPlayerPosLookPacket;
import me.ANONIMUS.proxy.utils.PacketUtil;


public class CommandFreecam extends Command {
    public CommandFreecam() {
        super("freecam", null, null, null, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        sender.setFreecam(!sender.isFreecam());

        if (sender.isFreecam()) {
            PacketUtil.changeGameMode(sender, Gamemode.SPECTATOR);
        } else {
            sender.getSession().sendPacket(new ServerPlayerPosLookPacket(
                    sender.getPos().getX(), sender.getPos().getY(), sender.getPos().getZ(), 180, 90)
            );
            PacketUtil.changeGameMode(sender, Gamemode.SURVIVAL);
        }
    }
}