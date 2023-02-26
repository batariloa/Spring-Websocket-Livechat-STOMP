package com.example.springwebsocketlivechat.controller;

import com.example.springwebsocketlivechat.manager.ChatroomUsersManager;
import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.service.ChatroomService;
import com.example.springwebsocketlivechat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {


    private final ChatroomUsersManager chatroomUsersManager;
    private final MessageService messageService;
    private final ChatroomService chatroomService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    private Message receivePublicMessage(@Payload Message message) {

        return message;
    }

    @MessageMapping("/chatroom/{room}/sendMessage")
    public void sendMessage(
            @DestinationVariable String room,
            @Payload Message message
    ) {
        // logic to handle the message and broadcast it to all subscribers
        messageService.sendMessageToChatroom(room, message);
    }

    @MessageMapping("/joinChatroom/{chatroomId}")
    public void joinChatroom(
            @Payload String username,
            @DestinationVariable String chatroomId,
            SimpMessageHeaderAccessor headerAccessor
    ) {

        if (headerAccessor.getSessionAttributes() == null) return;
        
        headerAccessor.getSessionAttributes()
                      .put("chatroomId", chatroomId);
        headerAccessor.getSessionAttributes()
                      .put("username", username);

        chatroomService.join(chatroomId, username);
    }


    @MessageMapping("/leaveChatroom")
    public void leaveChatroom(
            @Payload String chatroomId,
            SimpMessageHeaderAccessor headerAccessor
    ) {

        // Get the username from the user's session attributes
        if (headerAccessor.getSessionAttributes() != null) {
            String username = (String) headerAccessor.getSessionAttributes()
                                                     .get("username");
            chatroomService.leave(chatroomId, username);
        }
        // Remove the user from the specified chatroom


    }


    //Handle disconnect
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {


        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        if (headerAccessor.getSessionAttributes() == null) return;

        String username = (String) headerAccessor.getSessionAttributes()
                                                 .get("username");
        String chatroomId = (String) headerAccessor.getSessionAttributes()
                                                   .get("chatroomId");

        if (username != null) {

            chatroomService.leave(chatroomId, username);
        }
    }


    @GetMapping("/chatrooms")
    public ResponseEntity<Map<String, List<String>>> getChatrooms() {
        Map<String, List<String>> chatroomUsers = chatroomUsersManager.getAllChatroomUsers();
        return ResponseEntity.ok(chatroomUsers);
    }


}
