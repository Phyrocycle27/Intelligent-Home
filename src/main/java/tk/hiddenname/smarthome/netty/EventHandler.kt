package tk.hiddenname.smarthome.netty

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import org.slf4j.LoggerFactory

class EventHandler : ChannelDuplexHandler() {
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        val ch = ctx.channel()
        if (evt is IdleStateEvent) {
            if (evt.state() == IdleState.ALL_IDLE) {
                ctx.writeAndFlush("ping").addListener(ChannelFutureListener {
                    if (ch.pipeline().names().contains("msgTmpReader")) ch.pipeline().remove("msgTmpReader")
                    ch.pipeline().addBefore("sessionHandler", "msgTmpReader", object : ChannelInboundHandlerAdapter() {
                        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                            if (msg.toString() == "pong") {
                                log.info("PING IS OK!")
                            } else {
                                log.error("PING FAILED!!")
                            }
                        }

                        override fun channelReadComplete(ctx: ChannelHandlerContext) {
                            ch.pipeline().remove(this)
                        }

                        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                            log.error("Cause " + cause.message)
                            ch.pipeline().remove(this)
                            ctx.close()
                        }
                    })
                })
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(EventHandler::class.java)
    }
}