package me.ANONIMUS.proxy.protocol.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.List;

public class Varint21FrameDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        final byte[] bytes = new byte[3];

        for (int i = 0; i < bytes.length; ++i) {
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }

            bytes[i] = byteBuf.readByte();

            if (bytes[i] >= 0) {
                final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(bytes));
                try {
                    final int j = packetbuffer.readVarInt();
                    if (byteBuf.readableBytes() >= j) {
                        list.add(byteBuf.readBytes(j));
                        return;
                    }

                    byteBuf.resetReaderIndex();
                } finally {
                    packetbuffer.release();
                }
                return;
            }
        }

        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
