package tk.hiddenname.smarthome.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import tk.hiddenname.smarthome.Application;
import tk.hiddenname.smarthome.netty.security.CipherDecoder;
import tk.hiddenname.smarthome.netty.security.CipherEncoder;
import tk.hiddenname.smarthome.netty.security.Encryption;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final Encryption enc = new Encryption();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Active");
        ctx.channel().writeAndFlush(enc.getPublicKey());
        System.out.println("Channel id is: " + ctx.channel().localAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();

        if (!enc.isKeySet()) {
            enc.createSharedKey((byte[]) msg);
            System.out.println("Encryption AES key is: " + enc.isKeySet());

            ch.pipeline().addAfter("frameEncoder", "cipherDecoder", new CipherDecoder(enc));
            ch.pipeline().addAfter("cipherDecoder", "cipherEncoder", new CipherEncoder(enc));
            ch.pipeline().remove("bytesDecoder");
            ch.pipeline().remove("bytesEncoder");

            ch.writeAndFlush(Application.getToken());
        } else {
            System.out.println(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
