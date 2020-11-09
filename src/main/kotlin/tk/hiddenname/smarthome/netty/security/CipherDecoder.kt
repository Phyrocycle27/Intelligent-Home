package tk.hiddenname.smarthome.netty.security

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class CipherDecoder(private val enc: Encryption) : ByteToMessageDecoder() {
    override fun decode(channelHandlerContext: ChannelHandlerContext, buf: ByteBuf, list: MutableList<Any>) {
        val dataWithParams = ByteArray(buf.readableBytes())
        buf.readBytes(dataWithParams)
        val params = dataWithParams.copyOf(18)
        val data = dataWithParams.copyOfRange(18, dataWithParams.size)
        list.add(enc.decode(data, params))
    }
}