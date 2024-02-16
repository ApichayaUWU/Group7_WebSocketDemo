package com.websocket.demo.chat;


import lombok.Builder;
import lombok.Getter;

import static com.websocket.demo.config.WebSocketEventListener.onlineUsers;

@Getter
@Builder
public class ChatMessage {
    private String content;
    private String timestamp;
    private String sender;
    private MessageType type;
    private String count;

    public void updateOnlineUser(){
        this.count = onlineUsers+"";
    }

}
