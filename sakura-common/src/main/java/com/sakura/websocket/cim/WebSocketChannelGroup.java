package com.sakura.websocket.cim;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketChannelGroup {

    public final static ConcurrentHashMap<String, List<Channel>> channelMap = new ConcurrentHashMap<>();

    public final static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public void sendMessageAll(WebSocketMessageVO webSocketMessageVO) {
        channelGroup.writeAndFlush(webSocketMessageVO);
    }

    public static void sendMessage(WebSocketMessageVO messageVO) {
        List<Channel> channels = channelMap.get(messageVO.getId());
        if (channels == null) {
            log.error(messageVO.getId() + " : channel 不存在");
        } else {
            channels.forEach(channel -> {
                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageVO)));
            });
        }
    }

}
