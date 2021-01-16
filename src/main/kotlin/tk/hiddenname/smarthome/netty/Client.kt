package tk.hiddenname.smarthome.netty

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.slf4j.LoggerFactory
import javax.net.ssl.SSLException

class Client(private val HOST: String, private val PORT: Int) : Runnable {

    private val log = LoggerFactory.getLogger(Client::class.java.name)

    override fun run() {
        log.info("Netty Thread is just running")
        val group: EventLoopGroup = NioEventLoopGroup()
        createBootstrap(group)
    }

    fun createBootstrap(group: EventLoopGroup?) {
        val bootstrap = Bootstrap()
        log.info("Creating bootstrap")
        val sslCtx: SslContext
        try {
            sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()
            bootstrap.group(group)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel::class.java)
                .handler(ClientInitializer(sslCtx, HOST, PORT, this))
            bootstrap.connect(HOST, PORT).addListener(ConnectionListener(this))
        } catch (e: SSLException) {
            log.error(e.message)
        }
    }

    init {
        Thread(this, "Netty thread").start()
        log.info("Netty thread created")
    }
}