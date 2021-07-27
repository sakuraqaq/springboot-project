package com.sakura.websocket;

import java.util.HashMap;
import java.util.Map;

public class NetUtils {
    /**
     * 获取HTTP请求路径参数
     * @param url
     * @return
     */
    public static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<String,String>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }
}
