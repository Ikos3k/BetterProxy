package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.MessagePosition;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import me.ANONIMUS.proxy.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@NoArgsConstructor
@Data
public class ServerChatPacket extends Packet {

    {
        this.getProtocolList().add(new Protocol(0x02, 47));
        this.getProtocolList().add(new Protocol(0x0F, 110));
        this.getProtocolList().add(new Protocol(0x0F, 340));
    }

    private Component message;
    private MessagePosition position;

    public ServerChatPacket(String text) {
        this(Component.text(ChatUtil.fixColor(text)), MessagePosition.CHATBOX);
    }

    public ServerChatPacket(Component component) {
        this(component, MessagePosition.CHATBOX);
    }

    public ServerChatPacket(Component component, MessagePosition position) {
        this.message = component;
        this.position = position;
    }

    public ServerChatPacket(String text, MessagePosition position) {
        this(GsonComponentSerializer.gson().deserialize(text), position);
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(GsonComponentSerializer.gson().serialize(this.message));
        out.writeByte(position.getId());
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.message = GsonComponentSerializer.gson().deserialize(in.readString());
        this.position = MessagePosition.getById(in.readByte());
    }
}