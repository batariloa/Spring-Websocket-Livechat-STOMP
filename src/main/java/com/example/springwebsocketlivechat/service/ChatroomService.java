package com.example.springwebsocketlivechat.service;

import com.example.springwebsocketlivechat.manager.ChatroomUsersManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final ChatroomUsersManager chatroomUsersManager;

    public void leave(String chatroomId, String username){
        System.out.println("User leaving." + username);
        //remove user
        chatroomUsersManager.removeUserFromChatroom(chatroomId, username);
        //send new user list
        sendUserList(chatroomId);
        //send leave message
        messageService.sendLeaveMessageToChatRoom(chatroomId, username);

    }

    public void join(String chatroomId, String username){
        System.out.println("User joining: " + username);



        chatroomUsersManager.addUserToChatroom(chatroomId, username);
        sendUserList(chatroomId);
        messageService.sendJoinMessageToChatRoom(chatroomId, username);

    }


    public void sendUserList(String chatroomId){
        simpMessagingTemplate.convertAndSend("/chatroom/" + chatroomId + "/users", chatroomUsersManager.getUsersInChatroom(chatroomId));
    }

}
