package tk.hiddenname.smarthome.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Client implements Runnable {

    private final static Logger LOGGER;
    private final String HOST;
    private final int PORT;

    static {
        LOGGER = Logger.getLogger(Client.class.getName());
    }

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        new Thread(this, "Netty thread").start();
    }

    @Override
    public void run() {
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

            Channel channel = bootstrap.connect(HOST, PORT).sync().channel();

            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            for (;;) {
                String line = in.readLine();

                if (line == null) break;

                lastWriteFuture = channel.writeAndFlush(line);


                if (line.toLowerCase().equals("bye")) {
                    channel.closeFuture().sync();
                    break;
                }
            }

            if (lastWriteFuture != null) lastWriteFuture.sync();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
