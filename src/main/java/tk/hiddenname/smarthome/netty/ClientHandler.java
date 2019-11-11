package tk.hiddenname.smarthome.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import tk.hiddenname.smarthome.Application;
import tk.hiddenname.smarthome.netty.security.CipherDecoder;
import tk.hiddenname.smarthome.netty.security.CipherEncoder;
import tk.hiddenname.smarthome.netty.security.Encryption;

import java.io.IOException;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final Encryption enc = new Encryption();
    private static final CloseableHttpClient httpclient;

    static {
        httpclient = HttpClients.createDefault();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(enc.getPublicKey());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Channel ch = ctx.channel();

        if (!enc.isKeySet()) {
            enc.createSharedKey((byte[]) msg);

            ch.pipeline().addAfter("frameEncoder", "cipherDecoder", new CipherDecoder(enc));
            ch.pipeline().addAfter("cipherDecoder", "cipherEncoder", new CipherEncoder(enc));
            ch.pipeline().remove("bytesDecoder");
            ch.pipeline().remove("bytesEncoder");

            ch.writeAndFlush(Application.getToken());
        } else {
            // ***************** INTERNET ******************************
            JSONObject requestObj = new JSONObject(msg.toString());
            JSONObject requestBody = requestObj.getJSONObject("body");

            if (requestObj.getString("type").equals("request")) {

                HttpUriRequest request = null;

                switch (requestBody.getString("method")) {
                    case "GET":
                        request = new HttpGet(requestBody.getString("uri"));
                        break;
                    default:
                        System.out.println("Unknown type of the HTTP method");
                }

                request.addHeader("content-type", "application/json");

                try (CloseableHttpResponse response = httpclient.execute(request)) {
                    JSONObject responseBody = new JSONObject(
                            EntityUtils.toString(response.getEntity()));

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
        cause.printStackTrace();
        ctx.close();
    }
}
