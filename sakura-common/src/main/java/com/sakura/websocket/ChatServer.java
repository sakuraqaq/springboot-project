package com.sakura.websocket;

public class ChatServer {


    public static void main(String[] args) {
        try {
            new SockerServer(8081).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
