package tk.hiddenname.smarthome.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {

    private final static Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Client.class.getName());
    }

    private final String HOST;
    private final int PORT;
    private Channel channel;

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        new Thread(this, "Netty thread").start();
    }

    @Override
    public void run() {
        LOGGER.log(Level.INFO, "Netty Thread is running");
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
