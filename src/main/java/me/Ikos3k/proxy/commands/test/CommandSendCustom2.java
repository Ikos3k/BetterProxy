package me.Ikos3k.proxy.commands.test;

import me.Ikos3k.proxy.enums.ConnectedType;
import me.Ikos3k.proxy.objects.Command;
import me.Ikos3k.proxy.protocol.objects.Player;
import me.Ikos3k.proxy.protocol.packet.Packet.Builder.DataType;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;
import me.Ikos3k.proxy.protocol.packet.PacketDirection;
import me.Ikos3k.proxy.protocol.packet.impl.CustomPacket;
import me.Ikos3k.proxy.utils.PacketUtil;

import java.util.Arrays;

public class CommandSendCustom2 extends Command {
    public CommandSendCustom2() {
        super("sendcustom2", null, null, "[direction:SERVERBOUND/CLIENTBOUND]", ConnectedType.NONE);
    }

    //EXAMPLE #sendcustom2 CLIENTBOUND 2 STRING siema BYTE 0

    @Override
    public void onCommand(Player sender, String[] args) throws Exception {
        PacketDirection direction = PacketDirection.valueOf(args[1].toUpperCase());

        int packetID = Integer.parseInt(args[2]);

        PacketBuffer packetBuffer = PacketUtil.createEmptyPacketBuffer();

        for (int i = 3; i < args.length; i+=2) {
            try {
                DataType type = DataType.valueOf(args[i].toUpperCase());

                System.out.println("[DEBUG] " + type + " " + args[i + 1]);

                switch (type) {
                    case VARINT:
                        packetBuffer.writeVarInt(Integer.parseInt(args[i + 1]));
                        break;
                    case INT:
                        packetBuffer.writeInt(Integer.parseInt(args[i + 1]));
                        break;
                    case LONG:
                        packetBuffer.writeLong(Long.parseLong(args[i + 1]));
                        break;
                    case DOUBLE:
                        packetBuffer.writeDouble(Double.parseDouble(args[i + 1]));
                        break;
                    case FLOAT:
                        packetBuffer.writeFloat(Float.parseFloat(args[i + 1]));
                        break;
                    case BYTE:
                        packetBuffer.writeByte(Byte.parseByte(args[i + 1]));
                        break;
                    case SHORT:
                        packetBuffer.writeShort(Short.parseShort(args[i + 1]));
                        break;
                    case STRING:
                        StringBuilder out2 = new StringBuilder(args[i + 1]);
                        for (int d = i + 2; d < args.length; ++d) {
                            try {
                                DataType test = DataType.valueOf(args[d].toUpperCase());
                                break;
                            } catch (Exception e) {
                                out2.append(" ").append(args[d]);
                            }
                        }

                        System.out.println("TEST: " + out2);

                        packetBuffer.writeString(out2.toString());
                        break;
                    case BYTES:
                        StringBuilder out = new StringBuilder(args[3]);
                        for (int d = i + 1; d < args.length; ++d) {
                            out.append(" ").append(args[d]);
                        }
                        System.out.println(type + " " + args[i + 1] + " > " + out);

                        int[] test = Arrays.stream(out.toString().split(", ")).mapToInt(Integer::parseInt).toArray();

                        byte[] data = new byte[test.length];
                        for (int d = 0; d < test.length; d++) {
                            data[d] = (byte) test[d];
                        }
                        packetBuffer.writeBytes(data);
                        i = data.length;
                        break;
                }
            } catch (Exception ignored) {}
        }

        switch (direction) {
            case CLIENTBOUND:
                sender.getSession().sendPacket(new CustomPacket(packetID, packetBuffer.readByteArray()));
                break;
            case SERVERBOUND:
                sender.getRemoteSession().sendPacket(new CustomPacket(packetID, packetBuffer.readByteArray()));
                break;
        }
    }
}