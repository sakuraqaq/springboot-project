package com.sakura.websocket;

import com.sakura.entity.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;


public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端建立连接, 通道开启！");
        //往ChannelGroup中添加Channel
        MyChannelHandlerPool.channelGroup.add(ctx.channel());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("与客户端断开连接，通道关闭！");
        //移除掉ChannelGroup中的Channel 以及Map中的channel
       MyChannelHandlerPool.channelGroup.remove(ctx.channel());
        MyChanneMapManager.biDirectionHashMap.removeByValue(ctx.channel());
    }


    private void textdoMessage(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) {
        System.out.println(ctx.channel().id());
        MyChannelHandlerPool.sendAllMessage(textWebSocketFrame.text());
    }


    /**
     * websocket 首次肯定有一个http请求
     */
    protected void httpdoMessage(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {


        //创建websocket
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8080/ws", null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(fullHttpRequest);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), fullHttpRequest);
        }

        //取到一个uri 为一个协议的标识 比如websocket就是/ws
        String uri = fullHttpRequest.uri();

        //http请求后得到的一个用户id
        long i = MyChannelHandlerPool.i;

        User user = new User();
        user.setUserId(i);
        user.setUserName(i + "号用户");

        MyChannelHandlerPool.setChannelParameter(ctx, MyChannelHandlerPool.USER_INFO, user);

        if (!MyChanneMapManager.existConnectionByID(i)) {
            MyChanneMapManager.biDirectionHashMap.put(i, ctx.channel());
        }

        System.err.println("当前用户编号：" + i);

        MyChannelHandlerPool.i++;

        //如果url包含参数，需要处理
        if (uri.contains("?")) {
            String newUri = uri.substring(0, uri.indexOf("?"));
            System.out.println(newUri);
            fullHttpRequest.setUri(newUri);
        }

        System.out.println("当前在线人数：" + MyChannelHandlerPool.getOnlyNumsChannelGroup() + ";" + MyChanneMapManager.biDirectionHashMap.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        //首次连接是FullHttpRequest
        if (msg instanceof FullHttpRequest) {
            httpdoMessage(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof TextWebSocketFrame) {
            textdoMessage(ctx, (TextWebSocketFrame) msg);
        }
    }
}
