package com.websocket.demo.chat;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller // previous lab(RestApi) we used @RestController
public class chatController {
    //static Set<String> onlineUsers = new HashSet<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Endpoint of User to server
    @MessageMapping("/chat.sendMessage")
    // broadcast to this path
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessage;
    }


    // Endpoint of User to server
    @MessageMapping("/chat.addUser")
    // broadcast to this path
    @SendTo("/topic/public")
    // เอา user ไปเก็บที่ header ของ session ของการconnectของuser (HeaderAcessor)
    // + we can use session of user to identify each user
    public ChatMessage addUser(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // add username to headerAccessor
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.updateOnlineUser();
        return chatMessage;
    }


}
