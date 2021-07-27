package com.sakura.websocket;

import com.sakura.entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChannelHandlerPool {

    public static ChannelGroup channelGroup;

    public static final String USER_INFO = "userInfo";

    static {
        channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    public static int i = 0;


    /**
     * 获取在线人数
     */
    public static int getOnlyNumsChannelGroup(){
        return channelGroup.size();
    }

    /**
     * 设置通道参数
     */
    public static void setChannelParameter(ChannelHandlerContext channelHandlerContext, String key, Object vaule){
        channelHandlerContext.attr(AttributeKey.valueOf(key)).set(vaule);
    }

    /**
     * 设置通道参数
     */
    public static void setChannelParameter(Channel channel, String key, Object vaule){
        channel.attr(AttributeKey.valueOf(key)).set(vaule);
    }

    /**
     * 取出通道参数
     */
    public static Object getChannelParameter(ChannelHandlerContext channelHandlerContext, String key){

        return getChannelParameter(channelHandlerContext.channel(),key);
    }

    /**
     * 取出通道参数
     */
    public static Object getChannelParameter(Channel channel, String key){
        return channel.attr(AttributeKey.valueOf(key)).get();
    }

    /**
     * 向全体用户推送消息
     */
    public static void sendAllMessage(String message){
        MyChannelHandlerPool.channelGroup.writeAndFlush( new TextWebSocketFrame(message));
    }


    /**
     * 向指定用户推送消息
     */
    public static void sendMessgae(String message, Long userId){

        Channel channel = MyChanneMapManager.biDirectionHashMap.getByKey(userId);
        if(channel!=null){
            Object channelParameter = MyChannelHandlerPool.getChannelParameter(channel, MyChannelHandlerPool.USER_INFO);
            if(channelParameter!=null){
                User user = (User) channelParameter;
            }
            channel.writeAndFlush( new TextWebSocketFrame(message));
        }else{
            System.err.println("推送用户不存在");
        }


    }

}
