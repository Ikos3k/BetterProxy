package me.ANONIMUS.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ANONIMUS.proxy.protocol.data.TitleAction;
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
public class ServerTitlePacket extends Packet {
    private TitleAction titleAction;
    private Component title;
    private Component subTitle;
    private Component actionBar;

    private int fadeIn;
    private int fadeOut;
    private int stay;

    public ServerTitlePacket(TitleAction action, Component message) {
        this.titleAction = action;
        if (action == TitleAction.TITLE) {
            this.title = message;
        } else if (action == TitleAction.SUBTITLE) {
            this.subTitle = message;
        } else if (action == TitleAction.ACTIONBAR) {
            this.actionBar = message;
        } else {
            throw new IllegalArgumentException("Illegal use of ServerTitlePacket!");
        }
    }

    public ServerTitlePacket(TitleAction action, String message) {
        this(action, Component.text(ChatUtil.fixColor(message)));
    }

    public ServerTitlePacket(TitleAction action, int fadeIn, int stay, int fadeOut) {
        this.titleAction = action;
        if (titleAction == TitleAction.TIMES) {
            this.fadeIn = fadeIn;
            this.stay = stay;
            this.fadeOut = fadeOut;
        } else {
            throw new IllegalArgumentException("Illegal use of ServerTitlePacket");
        }
    }

    @Override
    public void write(PacketBuffer out, int protocol) throws Exception {
        out.writeVarInt(titleAction.getIdByProtocol(protocol));
        if (titleAction == TitleAction.TITLE) {
            out.writeString(GsonComponentSerializer.gson().serialize(this.title));
        } else if (titleAction == TitleAction.SUBTITLE) {
            out.writeString(GsonComponentSerializer.gson().serialize(this.subTitle));
        } else if (titleAction == TitleAction.ACTIONBAR) {
            out.writeString(GsonComponentSerializer.gson().serialize(this.actionBar));
        }
        if (titleAction == TitleAction.TIMES) {
            out.writeInt(this.fadeIn);
            out.writeInt(this.stay);
            out.writeInt(this.fadeOut);
        }
    }

    @Override
    public void read(PacketBuffer in, int protocol) throws Exception {
        this.titleAction = TitleAction.getById(in.readVarInt(), protocol);

        if (titleAction == TitleAction.TITLE) {
            this.title = GsonComponentSerializer.gson().deserialize(in.readString(32767));
        } else if (titleAction == TitleAction.SUBTITLE) {
            this.subTitle = GsonComponentSerializer.gson().deserialize(in.readString(32767));
        } else if (titleAction == TitleAction.ACTIONBAR) {
            this.actionBar = GsonComponentSerializer.gson().deserialize(in.readString(32767));
        } else if (titleAction == TitleAction.TIMES) {
            this.fadeIn = in.readInt();
            this.stay = in.readInt();
            this.fadeOut = in.readInt();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x45, 47), new Protocol(0x45, 110), new Protocol(0x48, 340));
    }
}