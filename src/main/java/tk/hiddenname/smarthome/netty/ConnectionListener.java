package tk.hiddenname.smarthome.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectionListener implements ChannelFutureListener {

    private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);
    private final Client client;

    public ConnectionListener(Client client) {
        this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) {
        if (!channelFuture.isSuccess()) {
            log.info("Reconnect");
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(() ->
                    client.createBootstrap(loop), 10L, TimeUnit.SECONDS);
        }
    }
}
