package me.Ikos3k.proxy.commands.test;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.PacketDirection;
import me.Ikos3k.proxy.protocol.packet.impl.CustomPacket;

import java.util.Arrays;

public class CommandSendCustom extends Command {
    public CommandSendCustom() {
        super("sendcustom", null, null, "[direction:SERVERBOUND/CLIENTBOUND] [packetid:int] [data:bytearray]", ConnectedType.NONE);
    }

    //EXAMPLE #sendcustom CLIENTBOUND 2 5, 115, 105, 101, 109, 97, 0

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        PacketDirection direction = PacketDirection.valueOf(args[1].toUpperCase());

        int packetID = Integer.parseInt(args[2]);

        StringBuilder out = new StringBuilder(args[3]);
        for (int i = 4; i < args.length; ++i) {
            out.append(" ").append(args[i]);
        }

        int[] test = Arrays.stream(out.toString().split(", "))
                .mapToInt(Integer::parseInt).toArray();

        System.out.println(Arrays.toString(test));

        byte[] data = new byte[test.length];

        for (int i = 0; i < test.length; i++) { data[i] = (byte) test[i]; }

        switch (direction) {
            case CLIENTBOUND:
                sender.getSession().sendPacket(new CustomPacket(packetID, data));
                break;
            case SERVERBOUND:
                sender.getRemoteSession().sendPacket(new CustomPacket(packetID, data));
                break;
        }
    }
}