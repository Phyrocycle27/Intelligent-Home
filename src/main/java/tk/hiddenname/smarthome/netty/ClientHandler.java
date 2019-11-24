package tk.hiddenname.smarthome.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final CloseableHttpClient httpclient;
    private static final Logger LOGGER;

    static {
        httpclient = HttpClients.createDefault();
        LOGGER = Logger.getLogger(ClientHandler.class.getName());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();

        JSONObject requestObj = new JSONObject(msg.toString());
        JSONObject jsonRequest = requestObj.getJSONObject("body");

        if (requestObj.getString("type").equals("request")) {

            HttpUriRequest request = null;

            switch (jsonRequest.getString("method")) {
                case "GET":
                    request = new HttpGet(jsonRequest.getString("uri"));
                    break;
                case "PUT":
                    request = new HttpPut(jsonRequest.getString("uri"));
                    break;
                case "POST":
                    HttpPost post = new HttpPost(jsonRequest.getString("uri"));
                    if (jsonRequest.keySet().contains("request_body")) {
                        StringEntity ent = new StringEntity(jsonRequest.getJSONObject("request_body").toString(),
                                StandardCharsets.UTF_8);
                        post.setEntity(ent);
                    }
                    request = post;
                    break;
                case "DELETE":
                    request = new HttpDelete(jsonRequest.getString("uri"));
                    break;
            }

            if (request != null) {

                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept-Charset", "UTF-8");

                try (CloseableHttpResponse response = httpclient.execute(request)) {
                    JSONObject responseBody = new JSONObject(
                            EntityUtils.toString(response.getEntity()))
                            .put("code", response.getStatusLine().getStatusCode());

                    JSONObject responseObj = new JSONObject()
                            .put("type", "data")
                            .put("body", responseBody);

                    ch.writeAndFlush(responseObj.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.log(Level.WARNING, cause.getMessage());
        ctx.close();
    }
}
