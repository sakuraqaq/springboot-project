package com.sakura.websocket;

import io.netty.channel.Channel;

public class MyChanneMapManager {


    public static BiDirectionHashMap<Long, Channel> biDirectionHashMap = new BiDirectionHashMap<>();

    /**
     * 连接是否存在
     */
    public static boolean existConnectionByID (Long id) {
        return biDirectionHashMap.containsKey(id);
    }

}
