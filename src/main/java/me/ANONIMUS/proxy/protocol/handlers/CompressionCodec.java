package me.ANONIMUS.proxy.protocol.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import me.ANONIMUS.proxy.protocol.packet.PacketBuffer;

import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Setter
@AllArgsConstructor
public class CompressionCodec extends ByteToMessageCodec<ByteBuf> {
    private final byte[] buffer = new byte[8192];
    private final Deflater deflater = new Deflater();
    private final Inflater inflater = new Inflater();
    private int compressionThreshold;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf in, ByteBuf out) {
        int readable = in.readableBytes();
        PacketBuffer output = new PacketBuffer(out);
        if (readable < this.compressionThreshold) {
            output.writeVarInt(0);
            out.writeBytes(in);
        } else {
            byte[] bytes = new byte[readable];
            in.readBytes(bytes);
            output.writeVarInt(bytes.length);
            this.deflater.setInput(bytes, 0, readable);
            this.deflater.finish();
            while (!this.deflater.finished()) {
                int length = this.deflater.deflate(this.buffer);
                output.writeBytes(buffer, length);
            }

            this.deflater.reset();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        if (buf.readableBytes() != 0) {
            PacketBuffer in = new PacketBuffer(buf);
            int size = in.readVarInt();
            if (size == 0) {
                out.add(buf.readBytes(buf.readableBytes()));
            } else {
                if (size < this.compressionThreshold) {
                    throw new DecoderException("Badly compressed packet: size of " + size + " is below threshold of " + this.compressionThreshold + ".");
                }

                if (size > 2097152) {
                    throw new DecoderException("Badly compressed packet: size of " + size + " is larger than protocol maximum of " + 2097152 + ".");
                }

                byte[] bytes = new byte[buf.readableBytes()];
                in.readBytes(bytes);
                this.inflater.setInput(bytes);
                byte[] inflated = new byte[size];
                this.inflater.inflate(inflated);
                out.add(Unpooled.wrappedBuffer(inflated));
                this.inflater.reset();
            }
        }
    }
}