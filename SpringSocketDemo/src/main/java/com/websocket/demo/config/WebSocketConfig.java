package com.websocket.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*
    * defined connection endpoint
    * */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // ใช้กำหนดpathที่client connect to server
        registry.addEndpoint("/ws") // connect via localhost:8080/ws
                .setAllowedOriginPatterns("*") // * allow ip from anywhere
                .withSockJS(); // using javascript library
    }


    /**
     * defined message passing endpoint
     * @param registry
     */
    @Override
    // use to define where(via) to receive and where to broadcast
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .setApplicationDestinationPrefixes("/app") // receive via localhost:8080/app
                .enableSimpleBroker("/topic"); // broadcast via chanel topic (but topic doesn't with url)
    }
}
