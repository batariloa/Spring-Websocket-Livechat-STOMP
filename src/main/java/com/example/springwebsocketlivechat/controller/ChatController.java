package com.example.springwebsocketlivechat.controller;

import com.example.springwebsocketlivechat.manager.ChatroomUsersManager;
import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.model.User;
import com.example.springwebsocketlivechat.service.ChatroomService;
import com.example.springwebsocketlivechat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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


    @MessageMapping("/chatroom/{room}/sendMessage")
    public void sendMessageToChatRoom(
            @DestinationVariable String room,
            @Payload Message message
    ) {
        // logic to handle the message and broadcast it to all subscribers
        // dummy change to trigger webhook
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

        String sessionId = headerAccessor.getSessionId();

        chatroomService.join(chatroomId, sessionId, username);
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

            chatroomService.leave(chatroomId, event.getSessionId());
        }
    }


    @GetMapping("/chatrooms")
    public ResponseEntity<Map<String, List<User>>> getChatRooms() {
        Map<String, List<User>> chatroomUsers = chatroomUsersManager.getAllChatroomUsers();
        return ResponseEntity.ok(chatroomUsers);
    }


}
