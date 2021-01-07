package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.MessagePosition;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import me.ANONIMUS.proxy.protocol.packet.Protocol;
import me.ANONIMUS.proxy.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class ServerChatPacket extends Packet {
    private Component message;
    private MessagePosition position;

    public ServerChatPacket(Component component) {
        this(component, MessagePosition.CHATBOX);
    }

    public ServerChatPacket(Component component, MessagePosition position) {
        this.message = component;
        this.position = position;
    }

    public ServerChatPacket(String text) {
        this(Component.text(ChatUtil.fixColor(text)), MessagePosition.CHATBOX);
    }

    public ServerChatPacket(String text, MessagePosition position) {
        this(Component.text(ChatUtil.fixColor(text)), position);
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

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x02, 47), new Protocol(0x0F, 110), new Protocol(0x0F, 340));
    }
}