package com.example.springwebsocketlivechat.service;


import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Clock;


@Service
@RequiredArgsConstructor
public class MessageService {
    private final SimpMessagingTemplate messagingTemplate;

    private final Clock clock;


    public void sendMessageToChatroom(
            String chatroomId,
            Message message
    ) {

        messagingTemplate.convertAndSend("/chatroom/" + chatroomId + "/messages", message);
    }

    public void sendJoinMessageToChatRoom(
            String chatroomId,
            String username
    ) {
        messagingTemplate.convertAndSend("/chatroom/" + chatroomId + "/messages", new Message("System", "User " + username + " has joined the room.", clock.instant()
                                                                                                                                                           .atZone(clock.getZone())
                                                                                                                                                           .toLocalTime(), Status.JOIN));
    }

    public void sendLeaveMessageToChatRoom(
            String chatroomId,
            String username
    ) {
        messagingTemplate.convertAndSend("/chatroom/" + chatroomId + "/messages", new Message("System", "User " + username + " has left the room.", clock.instant()
                                                                                                                                                         .atZone(clock.getZone())
                                                                                                                                                         .toLocalTime(), Status.LEAVE));
    }


}
