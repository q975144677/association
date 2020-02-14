//package com.association.configsend.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.association.common.all.util.log.ILogger;
//import com.association.configsend.app.Application;
//import com.association.configsend.component.SocketManager;
//import com.association.user.Iface.UserIface;
//import com.association.user.model.UserDO;
//import component.Proto;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import javax.websocket.Session;
//import java.io.IOException;
//import java.util.Map;
//
//@Component
//public class WebsocketEndPoint extends TextWebSocketHandler {
//    @Autowired
//    private ILogger logger;
//
//    @Autowired
//    SocketManager socketManager;
//    @Autowired
//    UserIface userIface;
//    @Override
//    // 接收到消息 调用
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        /**
//         * {
//         *     token : ""
//         * }
//         */
//        Application.flag = true ;
//        logger.info("连接：+1{}",message.getPayload());
//        String userMessage = message.getPayload();
//        try{
//            JSONObject jsonObject = JSONObject.parseObject(userMessage);
//            String token = jsonObject.getString("token");
//            if(StringUtils.isEmpty(token)){
//                logger.info("token + " + token);
//                return;
//            }
//            Proto<UserDO> userProto = userIface.getUserByToken(token);
//            if(userProto == null || userProto.getData() == null ){
//                logger.info("error:{EMPTY USER}");
//                return ;
//            }
//            UserDO userDO = userProto.getData();
//            socketManager.add(userDO.getGuid(),session);
//        }catch (Exception e){
//            logger.error("SOMETHING ERROR : {}" , e.getMessage());
//            return ;
//        }
//        super.handleTextMessage(session, message);
//        sendMessage("123", "d72886a83d2611ea85e78c04ba0c3a6d");
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        logger.info("afterConnectionEstablished");
//        super.afterConnectionEstablished(session);
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        logger.info("handleMessage");
//
//        super.handleMessage(session, message);
//    }
//
//    @Override
//    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
//        logger.info("handlePongMessage");
//        super.handlePongMessage(session, message);
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        logger.info("supportsPartialMessages");
//        return super.supportsPartialMessages();
//    }
//
//    //error
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        Map<String,WebSocketSession> map = socketManager.getManager();
//        for(Map.Entry<String,WebSocketSession> entry : map.entrySet()){
//            if(entry.getValue().getId().equals(session.getId())){
//                map.remove(entry.getKey());
//            }
//        }
//        super.handleTransportError(session, exception);
//    }
//
//    // 连接关闭
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        Map<String,WebSocketSession> map = socketManager.getManager();
//        for(Map.Entry<String,WebSocketSession> entry : map.entrySet()){
//            if(entry.getValue().getId().equals(session.getId())){
//                map.remove(entry.getKey());
//            }
//        }
//        super.afterConnectionClosed(session, status);
//
//    }
//
//    public void sendMessage(String message,String userGuid){
//        Map.Entry<String,WebSocketSession> entryset = socketManager.getManager().entrySet().stream().filter(entry -> userGuid.equals(entry.getKey())).findAny().orElse(null);
//        try {
//            if(entryset == null){
//                return ;
//            }
//            synchronized (entryset.getValue().getId()) {
//                System.out.println("SEND MESSAGE + "+message);
//                if(entryset.getValue().isOpen()) {
//                    System.out.println("OPENED SESSION");
//                    entryset.getValue().sendMessage(new TextMessage(message.getBytes()));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}