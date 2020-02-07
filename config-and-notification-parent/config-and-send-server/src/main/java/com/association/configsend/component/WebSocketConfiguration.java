package com.association.configsend.component;

import com.association.configsend.service.WebsocketEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    @Autowired
    WebsocketEndPoint websocketEndPoint;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(websocketEndPoint, "/websocket").setAllowedOrigins("*");
        registry.addHandler(websocketEndPoint, "/sockjs/websocket").withSockJS();
    }

//    @Bean
//    public WebSocketHandler myhandler() {
//        return new WebsocketEndPoint();
//    }
//
//    @Bean
//    public HandshakeInterceptor myInterceptors() {
//        return new HandshakeInterceptor();
//    }
}
