package com.sakura.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;

public class ClientServer {

    private final String socketUrl;


    public ClientServer(String socketUrl) {
        this.socketUrl = socketUrl;
    }

    public Channel start() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel){
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 1024 * 10))
                                .addLast("hookedHandler", new WebSocketClientHandler());
                    }
                });
        URI websocketURI = new URI(socketUrl);
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        //websocket握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, null, true, httpHeaders);
        Channel channel = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();
        WebSocketClientHandler handler = (WebSocketClientHandler) channel.pipeline().get("hookedHandler");
        handler.setHandshaker(handshaker);
        handshaker.handshake(channel);
        //阻塞等待是否握手成功
        handler.getChannelPromise().sync();
        System.out.println("握手成功");
        return channel;
    }

    public void sengMessage(Channel channel, String putMessage){
        //发送的内容，是一个文本格式的内容
        TextWebSocketFrame frame = new TextWebSocketFrame(putMessage);
        channel.writeAndFlush(frame).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("消息发送成功，发送的消息是："+putMessage);
            } else {
                System.out.println("消息发送失败 " + channelFuture.cause().getMessage());
            }
        });
    }

}

