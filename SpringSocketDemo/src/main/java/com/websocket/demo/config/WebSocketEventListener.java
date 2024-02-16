package com.websocket.demo.config;

import com.websocket.demo.chat.ChatMessage;
import com.websocket.demo.chat.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;



@Component // this neither controller nor configurator but still to be in Spring app
@RequiredArgsConstructor

// when we connect to server it's like sending message but when user disconnect it doesn't in the same case
// so we need to handle this event in another way (by using WebSocketEventListener not Controller)
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageSendingOperations;

    public static int onlineUsers = 0;

    @EventListener // use to detect the event(can both connect and disconnect)
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Stomp and Simp are protocal for sending information
        // Stomp mostly about get (here is "wrap ")
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        onlineUsers--;
        String str = onlineUsers+"";
        if (username != null) {
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .count(str)
                    .build();

            messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }



    @EventListener // use to detect the event(can both connect and disconnect)
    public void handleWebSocketConnectListener(SessionConnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        onlineUsers++;

        /* here is not necessary I guess */
        String str = onlineUsers+"";
        if (username != null) {
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.JOIN)
                    .sender(username)
                    .count(str)
                    .build();

            messageSendingOperations.convertAndSend("/topic/public", chatMessage);
        }
    }
}
