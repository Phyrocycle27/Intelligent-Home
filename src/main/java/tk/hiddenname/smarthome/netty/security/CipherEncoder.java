package tk.hiddenname.smarthome.netty.security;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CipherEncoder extends MessageToByteEncoder<String> {

    private final Encryption enc;

    public CipherEncoder(Encryption enc) {
        this.enc = enc;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf buf) {
        buf.writeBytes(enc.encode(s));
    }
}
