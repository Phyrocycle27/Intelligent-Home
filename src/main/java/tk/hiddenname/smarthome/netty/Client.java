package tk.hiddenname.smarthome.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
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

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        new Thread(this, "Netty thread").start();
        log.info("Netty thread created");
        System.out.println("Netty thread created");
    }

    @Override
    public void run() {
        log.info("Netty Thread is just running");
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        createBootstrap(group, bootstrap);
    }

    public void createBootstrap(EventLoopGroup group, Bootstrap bootstrap) {
        if (bootstrap != null) {
            log.info("Creating bootstrap");
            SslContext sslCtx;
            try {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

                bootstrap.group(group)
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .channel(NioSocketChannel.class)
                        .handler(new ClientInitializer(sslCtx, HOST, PORT, this));

                bootstrap.connect(HOST, PORT).addListener(new ConnectionListener(this));

            } catch (SSLException e) {
                e.printStackTrace();
            }
        }
    }
}
