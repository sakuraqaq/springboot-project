package com.sakura.websocket.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {


    private WebSocketClientHandshaker handshaker;
    private ChannelPromise channelPromise;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o){
        Channel channel = ctx.channel();
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                this.handshaker.finishHandshake(channel, (FullHttpResponse) o);
                this.channelPromise.setSuccess();
            } catch (WebSocketHandshakeException var7) {
                this.channelPromise.setFailure(new Exception(var7));
            }
        } else if (o instanceof FullHttpRequest) {
            throw new IllegalStateException("Unexpected FullHttpResponse ");
        } else {
            channel.writeAndFlush("发送一个消息到服务端").addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("发送成功");
                } else {
                    System.out.println("发送失败");
                }
            });
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("与服务端连接成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("与服务端断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("连接异常" + cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    public void handlerAdded(ChannelHandlerContext ctx) {
        this.channelPromise = ctx.newPromise();
    }


    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public void setChannelPromise(ChannelPromise channelPromise) {
        this.channelPromise = channelPromise;
    }

    public ChannelPromise getChannelPromise() {
        return channelPromise;
    }
}
