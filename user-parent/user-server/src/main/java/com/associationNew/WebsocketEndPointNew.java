//package com.associationNew;
//
//import com.alibaba.fastjson.JSONObject;
//import com.association.common.all.util.log.ILogger;
//import com.association.user.Iface.UserIface;
//import com.association.user.app.Application;
//import com.association.user.component.SocketManager;
//import com.association.user.model.UserDO;
//import component.Proto;
//import org.apache.commons.lang.StringUtils;
//import org.apache.tomcat.websocket.WsRemoteEndpointBasic;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.concurrent.Executors;
//
//@ServerEndpoint("/websocket")
//@Component
//public class WebsocketEndPointNew {
//    private ILogger logger;
//
//
//    SocketManager socketManager;
//    UserIface userIface;
//
//    void prepare(){
//        if(logger == null){
//            logger = Application.ctx.getBean(ILogger.class);
//        }
//        if(socketManager == null){
//            socketManager = Application.ctx.getBean(SocketManager.class);
//        }
//        if(userIface == null){
//            userIface = Application.ctx.getBean(UserIface.class);
//        }
//    }
//    @OnOpen
//    public void onopen(){
//        System.out.println("OPEN");
//    }
//    // 接收到消息 调用
//    @OnMessage
//    public void handleTextMessage(String message , Session session) throws Exception {
//        prepare();
//        /**
//         *
//         * {
//         *     token : ""
//         * }
//         */
//        logger.info("连接：+1{}",message);
//        String userMessage = message;
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
//        sendMessage("123", "d72886a83d2611ea85e78c04ba0c3a6d");
//    }
//
//
//
//
//    @OnError
//    public void handleTransportError(Session session, Throwable error) throws Exception {
//
//        prepare();
//        Map<String,Session> map = socketManager.getManager();
//        for(Map.Entry<String,Session> entry : map.entrySet()){
//            if(entry.getValue().getId().equals(session.getId())){
//                map.remove(entry.getKey());
//            }
//        }
//    }
//
//    // 连接关闭
//    @OnClose
//    public void afterConnectionClosed(Session session) throws Exception {
//        prepare();
//        Map<String,Session> map = socketManager.getManager();
//        for(Map.Entry<String,Session> entry : map.entrySet()){
//            if(entry.getValue().getId().equals(session.getId())){
//                map.remove(entry.getKey());
//            }
//        }
//
//    }
//
//    public void sendMessage(String message,String userGuid){
//        Executors.newCachedThreadPool().execute(()-> {
//            prepare();
//            Map.Entry<String, Session> entryset = socketManager.getManager().entrySet().stream().filter(entry -> userGuid.equals(entry.getKey())).findAny().orElse(null);
//            try {
//                if (entryset == null) {
//                    return;
//                }
//                synchronized (entryset.getValue().getId()) {
//                    System.out.println("SEND MESSAGE + " + message);
//                    if (entryset.getValue().isOpen()) {
//                        System.out.println("OPENED SESSION");
//                        LABEL :
//                        {
//                            WsRemoteEndpointBasic basic = (WsRemoteEndpointBasic) entryset.getValue().getBasicRemote();
//                            Arrays.stream(basic.getClass().getFields()).forEach(x-> System.out.println(x));
//                            Arrays.stream(basic.getClass().getMethods()).forEach(x-> System.out.println(x));
//                            try {
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        entryset.getValue().getBasicRemote().sendText(message);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//}
//
