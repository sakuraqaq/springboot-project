package com.sakura.websocket.cim;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("客户端连接：{}", ctx.channel().id());
        WebSocketChannelGroup.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String mapKey = "";
        WebSocketChannelGroup.channelGroup.remove(ctx.channel());
        for (String key : WebSocketChannelGroup.channelMap.keySet()) {
            if (WebSocketChannelGroup.channelMap.get(key).equals(ctx.channel())) {
                mapKey = key;
            }
        }
        WebSocketChannelGroup.channelMap.remove(mapKey);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            httpDoMessage(ctx, (FullHttpRequest) msg);
        }
    }

    protected void httpDoMessage(ChannelHandlerContext ctx, FullHttpRequest request) {

        //创建websocket
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("", null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            log.error("websocket 首次握手失败");
        } else {
            handshaker.handshake(ctx.channel(), request);
        }
        //获取前端传来的projectId
        String uri = request.uri();
        Map<String, String> paramMap = getUrlParams(uri);
        String id = paramMap.get("id");
        log.info("id------" + id);
        //如果url包含参数，需要处理
        if (uri.contains("?")) {
            String newUri = uri.substring(0, uri.indexOf("?"));
            request.setUri(newUri);
        }
        List<Channel> channelList;
        if (!WebSocketChannelGroup.channelMap.containsKey(id)) {
            channelList = new ArrayList<>();
            channelList.add(ctx.channel());
           WebSocketChannelGroup.channelMap.put(id, channelList);
        }else{
            channelList = WebSocketChannelGroup.channelMap.get(id);
            channelList.add(ctx.channel());
            WebSocketChannelGroup.channelMap.put(id, channelList);
        }
    }


    private static Map<String, String> getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;

        } else {
            return map;
        }
    }

}
