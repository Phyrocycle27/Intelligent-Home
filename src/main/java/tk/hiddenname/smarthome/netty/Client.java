package tk.hiddenname.smarthome.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;

public class Client implements Runnable {

    private final static Logger log;

    static {
        log = LoggerFactory.getLogger(Client.class.getName());
    }

    private final String HOST;
    private final int PORT;
    private Channel channel;

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        new Thread(this, "Netty thread").start();
        log.info("Netty thread created");
    }

    @Override
    public void run() {
        log.info("Netty Thread is just running");
        EventLoopGroup group = new NioEventLoopGroup();

        SslContext sslCtx = null;
        try {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } catch (SSLException e) {
            e.printStackTrace();
        }
        try {
            Bootstrap bootstrap = new io.netty.bootstrap.Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer(sslCtx, HOST, PORT));

            bootstrap.connect(HOST, PORT).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
