package me.Ikos3k.proxy.commands.test;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet;

import static me.Ikos3k.proxy.protocol.packet.Packet.Builder.DataType.*;

public class CommandTest extends Command {
    public CommandTest() {
        super("test", null, null, "[num]", ConnectedType.NONE);
    }

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        System.out.println("TEST" + args[1]);

        switch (Integer.parseInt(args[1])) {
            case 0:
                sender.getSession().sendPacket(new Packet.Builder()
                    .add(BYTES, new byte[] { 5, 115, 105, 101, 109, 97, 0 })
                .build(2));
                break;
            case 1:
                sender.getSession().sendPacket(new Packet.Builder()
                    .add(VARINT, 0)
                    .add(STRING, "test")
                .build(0x45));

                sender.getSession().sendPacket(new Packet.Builder()
                    .add(VARINT, 1)
                    .add(STRING, "lol")
                .build(0x45));

                sender.getSession().sendPacket(new Packet.Builder()
                    .add(VARINT, 2)
                    .add(INT, 20)
                    .add(INT, 5)
                    .add(INT, 20)
                .build(0x45));
                break;
        }
    }
}
