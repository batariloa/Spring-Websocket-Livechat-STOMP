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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ChatControllerTest {


    @Mock
    private MessageService messageService;

    @Mock
    Clock clock;

    private Clock fixedClock;


    @Mock
    private ChatroomService chatroomService;

    @InjectMocks
    private ChatController chatController;

    String chatRoom = "testRoom";
    String username = "username1";


    @Test
    public void testReceiveChatroomMessage() {
        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock)
                                      .instant();
        doReturn(fixedClock.getZone()).when(clock)
                                      .getZone();


        Message message = new Message("John", "Hello!", clock.instant()
                                                             .atZone(clock.getZone())
                                                             .toLocalTime(), Status.MESSAGE);


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
