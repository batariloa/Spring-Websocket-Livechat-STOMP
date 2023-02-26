package com.example.springwebsocketlivechat;


import com.example.springwebsocketlivechat.manager.ChatroomUsersManager;
import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final SimpMessagingTemplate messagingTemplate;

    private final ChatroomUsersManager chatroomUsersManager;

    public void sendMessageToChatroom(String chatroomId,String username, Message message) {

        message.setAuthor(username);
        List<String> users = chatroomUsersManager.getUsersInChatroom(chatroomId);
        System.out.println("USers in room "+  users.toArray().toString());
        if (users != null) {
            for (String user : users) {
                messagingTemplate.convertAndSendToUser(user, "/chatroom/" + chatroomId +"/messages", message);
            }
        }
    }

}
