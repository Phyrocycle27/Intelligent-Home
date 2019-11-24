package tk.hiddenname.smarthome.netty;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.ssl.SslContext;
import tk.hiddenname.smarthome.Application;
import tk.hiddenname.smarthome.netty.security.CipherDecoder;
import tk.hiddenname.smarthome.netty.security.CipherEncoder;
import tk.hiddenname.smarthome.netty.security.Encryption;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private final String HOST;
    private final int PORT;
    private final static Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(ClientInitializer.class.getName());
    }

    public ClientInitializer(SslContext sslCtx, String host, int port) {
        this.sslCtx = sslCtx;
        HOST = host;
        PORT = port;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        LOGGER.log(Level.INFO, "Channel initialization...");

        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                1048576, 0, 4, 0, 4
        ));
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("bytesDecoder", new ByteArrayDecoder());
        pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
        pipeline.addLast("auth", new ChannelInboundHandlerAdapter() {
            private final Encryption enc = new Encryption();

            @Override
            public void channelActive(ChannelHandlerContext ctx) {
                LOGGER.log(Level.INFO, "Channel active");
                ctx.channel().writeAndFlush(enc.getPublicKey());
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                Channel ch = ctx.channel();

                if (!enc.isKeySet()) {
                    enc.createSharedKey((byte[]) msg);

                    ch.pipeline().remove("bytesDecoder");
                    ch.pipeline().remove("bytesEncoder");
                    ch.pipeline().addAfter("frameEncoder", "cipherDecoder", new CipherDecoder(enc));
                    ch.pipeline().addAfter("cipherDecoder", "cipherEncoder", new CipherEncoder(enc));
                    ChannelFuture f = ch.writeAndFlush(Application.getToken());

                    // вот тут мы делаем такую штуку с помощью ChannelFuture, которая будет отсылать токен серверу и
                    // получать в ответ прошли мы аутентификацию или нет
                    try {
                        f.sync();
                        ch.pipeline().addAfter("cipherEncoder", "sessionHandler", new ClientHandler());
                        ch.pipeline().remove("auth");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                LOGGER.log(Level.WARNING, cause.getMessage());
                ctx.close();
            }
        });
    }
}
