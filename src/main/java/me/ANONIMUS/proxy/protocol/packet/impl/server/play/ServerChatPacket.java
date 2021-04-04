package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.Protocol;
import me.ANONIMUS.proxy.protocol.data.MessagePosition;
import me.ANONIMUS.proxy.protocol.packet.Packet;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class ServerChatPacket extends Packet {
    private BaseComponent[] message;
    private MessagePosition position;

    public ServerChatPacket(BaseComponent[] message, MessagePosition position) {
        this.message = message;
        this.position = position;
    }

    public ServerChatPacket(String message) {
        this(new ComponentBuilder(message).create(), MessagePosition.CHATBOX);
    }

    public ServerChatPacket(String message, MessagePosition position) {
        this(new ComponentBuilder(message).create(), position);
    }

    public ServerChatPacket(BaseComponent... text) {
        this(text, MessagePosition.CHATBOX);
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeString(ComponentSerializer.toString(message));
        out.writeByte(position.getId());
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.message = ComponentSerializer.parse(in.readString());
        this.position = MessagePosition.getById(in.readByte());
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x02, 47), new Protocol(0x0F, 109), new Protocol(0x0F, 110), new Protocol(0x0F, 210), new Protocol(0x0F, 340));
    }
}