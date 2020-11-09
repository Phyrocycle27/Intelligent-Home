package tk.hiddenname.smarthome.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder
import io.netty.handler.ssl.SslContext
import io.netty.handler.timeout.IdleStateHandler
import org.slf4j.LoggerFactory
import tk.hiddenname.smarthome.Application
import tk.hiddenname.smarthome.netty.security.CipherDecoder
import tk.hiddenname.smarthome.netty.security.CipherEncoder
import tk.hiddenname.smarthome.netty.security.Encryption

class ClientInitializer(private val sslCtx: SslContext, private val HOST: String, private val PORT: Int, private val client: Client) : ChannelInitializer<SocketChannel>() {

    private val log = LoggerFactory.getLogger(ClientInitializer::class.java.name)

    override fun initChannel(sch: SocketChannel) {
        log.info("Channel initialization...")
        val pipeline = sch.pipeline()
        pipeline.addLast(sslCtx.newHandler(sch.alloc(), HOST, PORT))
                .addLast("frameDecoder", LengthFieldBasedFrameDecoder(
                        1048576, 0, 4, 0, 4))
                .addLast("frameEncoder", LengthFieldPrepender(4))
                .addLast("bytesDecoder", ByteArrayDecoder())
                .addLast("bytesEncoder", ByteArrayEncoder())
        pipeline.addLast("auth", object : ChannelInboundHandlerAdapter() {
            private val enc = Encryption()
            override fun channelActive(ctx: ChannelHandlerContext) {
                log.info("Channel active")
                ctx.channel().writeAndFlush(enc.getPublicKey())
            }

            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                val ch = ctx.channel()
                if (!enc.isKeySet()) {
                    enc.createSharedKey(msg as ByteArray)
                    ch.pipeline().remove("bytesDecoder")
                    ch.pipeline().remove("bytesEncoder")
                    ch.pipeline().addAfter("frameEncoder", "cipherDecoder", CipherDecoder(enc))
                    ch.pipeline().addAfter("cipherDecoder", "cipherEncoder", CipherEncoder(enc))
                    val f = ch.writeAndFlush(Application.token)

                    // вот тут мы делаем такую штуку с помощью ChannelFuture, которая будет отсылать токен серверу и
                    // получать в ответ прошли мы аутентификацию или нет
                    try {
                        f.sync()
                        ch.pipeline().addAfter("cipherEncoder", "idleHandler",
                                IdleStateHandler(0, 0, 120))
                        ch.pipeline().addAfter("idleHandler", "eventHandler", EventHandler())
                        ch.pipeline().addAfter("eventHandler", "sessionHandler", ClientHandler(client))
                        ch.pipeline().remove("auth")
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                log.info(cause.message)
            }
        })
    }

    init {
        log.info(String.format("Server's host is %s and port is %d", HOST, PORT))
    }
}