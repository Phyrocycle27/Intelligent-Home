package tk.hiddenname.smarthome.netty;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventHandler extends ChannelDuplexHandler {

    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        Channel ch = ctx.channel();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.ALL_IDLE) {

                ctx.writeAndFlush("ping").addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) {
                        if (ch.pipeline().names().contains("msgTmpReader"))
                            ch.pipeline().remove("msgTmpReader");

                        ch.pipeline().addBefore("sessionHandler", "msgTmpReader", new ChannelInboundHandlerAdapter() {

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                if (msg.toString().equals("pong")) {
                                    log.info("PING IS OK!");
                                } else {
                                    log.error("PING FAILED!!");
                                }
                            }

                            @Override
                            public void channelReadComplete(ChannelHandlerContext ctx) {
                                ch.pipeline().remove(this);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                log.error("Cause " + cause.getMessage());
                                ch.pipeline().remove(this);
                                ctx.close();
                            }
                        });
                    }
                });
            }
        }
    }
}
