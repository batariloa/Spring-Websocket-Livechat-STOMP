package com.example.springwebsocketlivechat.unit;


import com.example.springwebsocketlivechat.controller.ChatController;
import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.model.Status;
import com.example.springwebsocketlivechat.service.ChatroomService;
import com.example.springwebsocketlivechat.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ChatControllerTest {


    @Mock
    private MessageService messageService;

    @Mock
    private ChatroomService chatroomService;

    @InjectMocks
    private ChatController chatController;

    String chatRoom = "testRoom";
    String username = "username1";


    @Test
    public void testReceiveChatroomMessage() {

        Message message = new Message("John", "Hello!", String.valueOf(LocalDateTime.now()), Status.MESSAGE);


        chatController.sendMessageToChatRoom(chatRoom, message);

        verify(messageService, times(1)).sendMessageToChatroom(eq(chatRoom), eq(message));
    }

    @Test
    public void testJoinChatRoomHeaderAccessorUsername() {


        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.create();
        simpMessageHeaderAccessor.setSessionAttributes(new HashMap<>());
        chatController.joinChatroom(username, chatRoom, simpMessageHeaderAccessor);

        Assertions.assertEquals(Objects.requireNonNull(simpMessageHeaderAccessor.getSessionAttributes())
                                       .get("username"), username);

    }


}
