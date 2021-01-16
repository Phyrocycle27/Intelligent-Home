package tk.hiddenname.smarthome.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class ClientHandler(private val client: Client) : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val ch = ctx.channel()
        log.info("new message $msg")
        val requestObj = JSONObject(msg.toString())
        val jsonRequest = requestObj.getJSONObject("body")
        if (requestObj.getString("type") == "request") {
            var request: HttpUriRequest? = null
            when (jsonRequest.getString("method")) {
                "GET" -> request = HttpGet(jsonRequest.getString("uri"))
                "PUT" -> {
                    val put = HttpPut(jsonRequest.getString("uri"))
                    if (jsonRequest.keySet().contains("request_body")) {
                        val ent = StringEntity(
                            jsonRequest.getJSONObject("request_body").toString(),
                            StandardCharsets.UTF_8
                        )
                        put.entity = ent
                    }
                    put.addHeader("Content-Type", "application/json")
                    request = put
                }
                "POST" -> {
                    val post = HttpPost(jsonRequest.getString("uri"))
                    if (jsonRequest.keySet().contains("request_body")) {
                        val ent = StringEntity(
                            jsonRequest.getJSONObject("request_body").toString(),
                            StandardCharsets.UTF_8
                        )
                        post.entity = ent
                    }
                    post.addHeader("Content-Type", "application/json")
                    request = post
                }
                "DELETE" -> request = HttpDelete(jsonRequest.getString("uri"))
            }
            if (request != null) {
                try {
                    HttpClients.createDefault().use { httpClient ->
                        httpClient.execute(request).use { response ->
                            val responseBody: JSONObject
                            val entity: String
                            if (response.entity != null) {
                                entity = EntityUtils.toString(response.entity)
                                responseBody = JSONObject()
                                if (entity.startsWith("[")) {
                                    responseBody.put("entity", JSONArray(entity))
                                } else responseBody.put("entity", JSONObject(entity))
                                log.debug("Received request is: $entity")
                            } else responseBody = JSONObject()
                            responseBody.put("code", response.statusLine.statusCode)
                            val responseObj = JSONObject()
                                .put("type", "data")
                                .put("body", responseBody)
                            ch.writeAndFlush(responseObj.toString())
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @Throws(Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        log.info("Unregistering")
        super.channelInactive(ctx)
        reconnect(ctx)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        log.error("ClientHandler Error: " + cause.message)
        super.exceptionCaught(ctx, cause)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("channel inactive")
        super.channelInactive(ctx)
    }

    private fun reconnect(ctx: ChannelHandlerContext) {
        val eventLoop = ctx.channel().eventLoop()
        eventLoop.schedule({ client.createBootstrap(eventLoop) }, 10L, TimeUnit.SECONDS)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ClientHandler::class.java)
    }
}