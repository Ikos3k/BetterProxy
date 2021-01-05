package me.ANONIMUS.proxy.protocol.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

public class VarInt21FrameEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        final int size = byteBuf.readableBytes();
        final int j = PacketBuffer.getVarIntSize(size);
        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + size + " into 3");
        }

        final PacketBuffer packetbuffer = new PacketBuffer(byteBuf2);
        packetbuffer.ensureWritable(j + size);
        packetbuffer.writeVarInt(size);
        packetbuffer.writeBytes(byteBuf, byteBuf.readerIndex(), size);
    }
}