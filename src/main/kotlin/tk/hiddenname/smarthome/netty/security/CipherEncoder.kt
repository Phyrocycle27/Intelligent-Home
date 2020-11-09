package tk.hiddenname.smarthome.netty.security

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class CipherEncoder(private val enc: Encryption) : MessageToByteEncoder<String>() {
    override fun encode(channelHandlerContext: ChannelHandlerContext, s: String, buf: ByteBuf) {
        buf.writeBytes(enc.encode(s))
    }
}