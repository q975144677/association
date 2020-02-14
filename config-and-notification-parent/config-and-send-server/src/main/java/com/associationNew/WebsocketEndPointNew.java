package com.associationNew;

import com.alibaba.fastjson.JSONObject;
import com.association.common.all.util.log.ILogger;
import com.association.configsend.app.Application;
import com.association.configsend.component.SocketManager;
import com.association.user.Iface.UserIface;
import com.association.user.model.UserDO;
import com.sun.jmx.snmp.tasks.ThreadService;
import component.Proto;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.websocket.WsRemoteEndpointBase;
import org.apache.tomcat.websocket.WsRemoteEndpointBasic;
import org.apache.tomcat.websocket.WsRemoteEndpointImplBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@ServerEndpoint("/websocket")
@Component
public class WebsocketEndPointNew {
    private ILogger logger;

//com.association.configsend.component.SocketManager socketManager= new com.association.configsend.component.SocketManager();
  static  SocketManager socketManager = new SocketManager();
    UserIface userIface;
Session session;
    void prepare(){
        if(logger == null){
            logger = Application.ctx.getBean(ILogger.class);
        }
        if(socketManager == null){
//            socketManager = Application.ctx.getBean(SocketManager.class);
        }
        if(userIface == null){
            userIface = Application.ctx.getBean(UserIface.class);
        }
    }
    @OnOpen
    public void onopen(){
        System.out.println("OPEN");
    }
    // 接收到消息 调用
    @OnMessage
    public void handleTextMessage(String message , Session session) throws Exception {

//        session.getBasicRemote().sendText("t"); session.getBasicRemote().sendText("t");
        prepare();
        /**
         *
         * {
         *     token : ""
         * }
         */
        logger.info("连接：+1{}",message);
        String userMessage = message;
        try{
            JSONObject jsonObject = JSONObject.parseObject(userMessage);
            String token = jsonObject.getString("token");
            if(StringUtils.isEmpty(token)){
                logger.info("token + " + token);
                return;
            }
            Proto<UserDO> userProto = userIface.getUserByToken(token);
            if(userProto == null || userProto.getData() == null ){
                logger.info("error:{EMPTY USER}");
                return ;
            }
            UserDO userDO = userProto.getData();
            logger.info("socketADD" + JSONObject.toJSONString(userDO));
            socketManager.add(userDO.getGuid(),session);
           // socketManager.add(userDO.getGuid(),session); // 很奇怪 这里导致 STREAM_WRITING 了 原先是放在别的spring bean里的，会出奇怪的问题
        }catch (Exception e){
            e.printStackTrace();
            logger.error("SOMETHING ERROR : {}" , e.getMessage());
            return ;
        }
//        session.getBasicRemote().sendText("t"); session.getBasicRemote().sendText("t");

//        sendMessage("123", "d72886a83d2611ea85e78c04ba0c3a6d");
    }




    @OnError
    public void handleTransportError(Session session, Throwable error) throws Exception {
        System.out.println("报错：" +session.getId()  + "throw" + error.getMessage());
        prepare();
        Map<String,Session> map = socketManager.getManager();
        for(Map.Entry<String,Session> entry : map.entrySet()){
            if(entry.getValue().getId().equals(session.getId())){
                map.remove(entry.getKey());
            }
        }
    }

    // 连接关闭
    @OnClose
    public void afterConnectionClosed(Session session) throws Exception {
        prepare();
        System.out.println("连接断开 + " + session.getId());
        Map<String,Session> map = socketManager.getManager();
        for(Map.Entry<String,Session> entry : map.entrySet()){
            if(entry.getValue().getId().equals(session.getId())){
                map.remove(entry.getKey());
            }
        }

    }

    public synchronized void sendMessage(String message,String userGuid){
            prepare();
            Map.Entry<String, Session> entryset = socketManager.getManager().entrySet().stream().filter(entry -> userGuid.equals(entry.getKey())).findAny().orElse(null);
            try {
                if (entryset == null) {
                    System.out.println(entryset + "NULL ENTRY");
                    return;
                }
                System.out.println("entryset :" + entryset.getKey());
                System.out.println("SEND MESSAGE + " + message);
                    if (entryset.getValue().isOpen()) {
                        System.out.println("OPENED SESSION");
                        entryset.getValue().getBasicRemote().sendText(message);
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    @Data
    static class SocketManager {
//        ILogger logger;
        private ConcurrentHashMap<String, Session> manager = new ConcurrentHashMap<>();

        public void add(String key, Session webSocketSession) {
//            logger.info("新添加webSocket连接 {} ", key);
            manager.put(key, webSocketSession);
        }

        public void remove(String key) {
//            logger.info("移除webSocket连接 {} ", key);
            manager.remove(key);
        }

        public Session get(String key) {
//            logger.info("获取webSocket连接 {}", key);
            return manager.get(key);
        }



    }
}

