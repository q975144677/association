//package com.association.user.component;
//
//import com.association.common.all.util.log.ILogger;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.websocket.Session;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//@Data
//public class SocketManager {
//    @Autowired
//    ILogger logger;
//    private ConcurrentHashMap<String, Session> manager = new ConcurrentHashMap<>();
//
//    public void add(String key, Session webSocketSession) {
//        logger.info("新添加webSocket连接 {} ", key);
//        manager.put(key, webSocketSession);
//    }
//
//    public void remove(String key) {
//        logger.info("移除webSocket连接 {} ", key);
//        manager.remove(key);
//    }
//
//    public Session get(String key) {
//        logger.info("获取webSocket连接 {}", key);
//        return manager.get(key);
//    }
//
//
//}
