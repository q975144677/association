package com.association.configsend.component;

import com.association.common.all.util.log.ILogger;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Component
@Data
public class SocketManager {
    @Autowired
    ILogger logger;
    private ConcurrentHashMap<String, WebSocketSession> manager = new ConcurrentHashMap<String, WebSocketSession>();

    public void add(String key, WebSocketSession webSocketSession) {
        logger.info("新添加webSocket连接 {} ", key);
        manager.put(key, webSocketSession);
    }

    public void remove(String key) {
        logger.info("移除webSocket连接 {} ", key);
        manager.remove(key);
    }

    public WebSocketSession get(String key) {
        logger.info("获取webSocket连接 {}", key);
        return manager.get(key);
    }


}
