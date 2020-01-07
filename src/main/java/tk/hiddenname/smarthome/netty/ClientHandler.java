package tk.hiddenname.smarthome.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log;

    static {
        log = LoggerFactory.getLogger(ClientHandler.class);
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
                    HttpPut put = new HttpPut(jsonRequest.getString("uri"));
                    if (jsonRequest.keySet().contains("request_body")) {
                        StringEntity ent = new StringEntity(jsonRequest.getJSONObject("request_body").toString(),
                                StandardCharsets.UTF_8);
                        put.setEntity(ent);
                    }
                    put.addHeader("Content-Type", "application/json");
                    request = put;
                    break;
                case "POST":
                    HttpPost post = new HttpPost(jsonRequest.getString("uri"));
                    if (jsonRequest.keySet().contains("request_body")) {
                        StringEntity ent = new StringEntity(jsonRequest.getJSONObject("request_body").toString(),
                                StandardCharsets.UTF_8);
                        post.setEntity(ent);
                    }
                    post.addHeader("Content-Type", "application/json");
                    request = post;
                    break;
                case "DELETE":
                    request = new HttpDelete(jsonRequest.getString("uri"));
                    break;
            }

            if (request != null) {
                try (CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(request)) {

                    JSONObject responseBody;
                    String entity;

                    if (response.getEntity() != null) {
                        entity = EntityUtils.toString(response.getEntity());
                        responseBody = new JSONObject();
                        if (entity.startsWith("[")) {
                            responseBody.put("entity", new JSONArray(entity));
                        } else responseBody.put("entity", new JSONObject(entity));
                        log.debug("Received request is: " + entity);
                    } else responseBody = new JSONObject();

                    responseBody.put("code", response.getStatusLine().getStatusCode());

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
        log.error("ClientHandler Error", cause);
        ctx.close();
    }
}
