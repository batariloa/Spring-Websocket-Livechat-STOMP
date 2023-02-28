package com.example.springwebsocketlivechat.unit;

import com.example.springwebsocketlivechat.manager.ChatroomUsersManager;
import com.example.springwebsocketlivechat.model.User;
import com.example.springwebsocketlivechat.service.ChatroomService;
import com.example.springwebsocketlivechat.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ChatroomServiceTest {


    @Mock
    ChatroomUsersManager chatroomUsersManager;

    @Mock
    MessageService messageService;

    @Mock
    SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    ChatroomService chatroomService;

    String chatroomId = "testChatroom";
    String sessionId = "sessionID";
    String username = "user123";

    @Test
    void joinChatroomTest() {

        chatroomService.join(chatroomId, sessionId, username);

        // assert that join method calls user manager to add user to the room
        verify(chatroomUsersManager, times(1)).addUserToChatroom(chatroomId, sessionId, username);
        // assert that join method calls message service to send join message to chatroom
        verify(messageService, times(1)).sendJoinMessageToChatRoom(chatroomId, username);
    }

    @Test
    void leaveChatroomTest() {

        //return username mapped to sessionId
        doReturn(username).when(chatroomUsersManager)
                          .getUsernameForSession(sessionId);
        //call the mocked method
        chatroomService.leave(chatroomId, sessionId);
        // assert that join method calls user manager to add user to the room
        verify(chatroomUsersManager, times(1)).removeUserFromChatroom(chatroomId, sessionId);
        // assert that join method calls message service to send join message to chatroom
        verify(messageService, times(1)).sendLeaveMessageToChatRoom(chatroomId, username);
    }

    @Test
    void sendUserList() {

        //mock user list
        List<User> users = List.of(new User());

        //when calling the user manager for room members, return the mocked list
        doReturn(users).when(chatroomUsersManager)
                       .getUsersInChatroom(chatroomId);

        //call the tested method
        chatroomService.sendUserList(chatroomId);

        //verify it calls messaging template with correct parameters
        verify(simpMessagingTemplate, times(1)).convertAndSend("/chatroom/" + chatroomId + "/users", users);
    }


}
