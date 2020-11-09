package tk.hiddenname.smarthome.netty

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ConnectionListener(private val client: Client) : ChannelFutureListener {

    private val log = LoggerFactory.getLogger(ConnectionListener::class.java)

    override fun operationComplete(channelFuture: ChannelFuture) {
        if (!channelFuture.isSuccess) {
            log.info("Reconnect")
            val loop = channelFuture.channel().eventLoop()
            loop.schedule({ client.createBootstrap(loop) }, 10L, TimeUnit.SECONDS)
        }
    }
}