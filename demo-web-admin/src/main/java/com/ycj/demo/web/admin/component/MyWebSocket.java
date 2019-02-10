package com.ycj.demo.web.admin.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ycj.demo.result.Result;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/webSocket/{name}")
@Component
@Slf4j
public class MyWebSocket {

    private static final String HEART = "heart";

    @Getter
    private String name;

    /**
     * 使用用户名标识每个客户端
     */
    private static ConcurrentHashMap<String, MyWebSocket> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam("name") String name, Session session) {
        this.session = session;
        this.name = name;

        webSocketMap.put(name, this);

        log.info("set => " + name + " for webSocketSet, current size: " +  webSocketMap.size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(this.getName());
        log.info("remove => " + name + " for webSocketSet, current size: " +  webSocketMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject object = JSONUtil.parseObj(message);
        if(object.containsKey(HEART)){
            log.info("Receive client ["+ this.getName() +"] heart ");
        }else{
            log.info("Receive client ["+ this.getName() +"] messages : " + message);
        }
    }

    /**
     * 发生错误时调用
     */
     @OnError
     public void onError(Session session, Throwable error) {
        log.error("error: "+error.getMessage());
     }


     public void sendMessage(Result message) {
         log.info("send message: " + message);
         //同步发送消息getBasicRemote()
         //异步发送消息getAsyncRemote()
         this.session.getAsyncRemote().sendText(JSONUtil.parseObj(message).toString());
     }


     /**
      * 群发自定义消息
      * */
    public static void sendInfo(String name, Result message){
        MyWebSocket socket = webSocketMap.get(name);
        if(null != socket){
            socket.sendMessage(message);
        }
    }

    public static void sendInfo(Result message) {
        for (MyWebSocket socket : webSocketMap.values()) {
            socket.sendMessage(message);
        }
    }
}