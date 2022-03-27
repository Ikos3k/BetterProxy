package me.Ikos3k.proxy.protocol.packet.impl.server.play;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.Ikos3k.proxy.protocol.Protocol;
import me.Ikos3k.proxy.protocol.data.TitleAction;
import me.Ikos3k.proxy.protocol.packet.Packet;
import me.Ikos3k.proxy.protocol.packet.PacketBuffer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.chat.ComponentSerializer;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class ServerTitlePacket extends Packet {
    private TitleAction titleAction;
    private BaseComponent[] title;
    private BaseComponent[] subTitle;
    private BaseComponent[] actionBar;

    private int fadeIn;
    private int fadeOut;
    private int stay;

    public ServerTitlePacket(TitleAction action, String message) {
        this.titleAction = action;
        if (action == TitleAction.TITLE) {
            this.title = new ComponentBuilder(message).create();
        } else if (action == TitleAction.SUBTITLE) {
            this.subTitle = new ComponentBuilder(message).create();
        } else if (action == TitleAction.ACTIONBAR) {
            this.actionBar = new ComponentBuilder(message).create();
        } else {
            throw new IllegalArgumentException("Illegal use of ServerTitlePacket!");
        }
    }

    public ServerTitlePacket(TitleAction action, BaseComponent... components) {
        this.titleAction = action;
        if (action == TitleAction.TITLE) {
            this.title = components;
        } else if (action == TitleAction.SUBTITLE) {
            this.subTitle = components;
        } else if (action == TitleAction.ACTIONBAR) {
            this.actionBar = components;
        } else {
            throw new IllegalArgumentException("Illegal use of ServerTitlePacket!");
        }
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
            out.writeString(ComponentSerializer.toString(this.title));
        } else if (titleAction == TitleAction.SUBTITLE) {
            out.writeString(ComponentSerializer.toString(this.subTitle));
        } else if (titleAction == TitleAction.ACTIONBAR) {
            out.writeString(ComponentSerializer.toString(this.actionBar));
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
            this.title = ComponentSerializer.parse(in.readString());
        } else if (titleAction == TitleAction.SUBTITLE) {
            this.subTitle = ComponentSerializer.parse(in.readString());
        } else if (titleAction == TitleAction.ACTIONBAR) {
            this.actionBar = ComponentSerializer.parse(in.readString());
        } else if (titleAction == TitleAction.TIMES) {
            this.fadeIn = in.readInt();
            this.stay = in.readInt();
            this.fadeOut = in.readInt();
        }
    }

    @Override
    public List<Protocol> getProtocolList() {
        return Arrays.asList(new Protocol(0x45, 47, 107, 108, 109, 110, 210, 315, 316), new Protocol(0x47, 335), new Protocol(0x48, 338, 340));
    }
}