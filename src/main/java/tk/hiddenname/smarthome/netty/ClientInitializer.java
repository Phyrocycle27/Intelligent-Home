package tk.hiddenname.smarthome.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.ssl.SslContext;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private final String HOST;
    private final int PORT;

    public ClientInitializer(SslContext sslCtx, String host, int port) {
        this.sslCtx = sslCtx;
        HOST = host;
        PORT = port;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                1048576, 0, 4, 0, 4
        ));
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
        pipeline.addLast("bytesDecoder", new ByteArrayDecoder());
        pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
        pipeline.addLast("chat", new ClientHandler());
    }
}
