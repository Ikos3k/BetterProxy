package me.Ikos3k.proxy.commands.cheats;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.data.Gamemode;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerDestroyEntitiesPacket;
import me.Ikos3k.proxy.protocol.packet.impl.server.play.ServerPlayerPosLookPacket;
import me.Ikos3k.proxy.utils.PacketUtil;

public class CommandFreecam extends Command {
    public CommandFreecam() {
        super("freecam", null, null, null, ConnectedType.CONNECTED);
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        sender.setFreecam(!sender.isFreecam());

        if (sender.isFreecam()) {
            PacketUtil.changeGameMode(sender, Gamemode.SPECTATOR);
            sender.getSession().sendPacket(new ServerDestroyEntitiesPacket(sender.getEntityId()));
        } else {
            sender.getSession().sendPacket(new ServerPlayerPosLookPacket(
                sender.getPos().getX(), sender.getPos().getY(), sender.getPos().getZ(), 180, 90)
            );
            PacketUtil.changeGameMode(sender, Gamemode.SURVIVAL);
        }
    }
}