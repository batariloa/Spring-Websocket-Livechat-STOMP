package com.example.springwebsocketlivechat.unit;

import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.model.Status;
import com.example.springwebsocketlivechat.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private Clock clock;

    @InjectMocks
    private MessageService messageService;

    String username = "sdasdsa";
    String chatroomId = "chatroom";

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendMessageToChatroomTest() {
        String chatroomId = "1";
        Message message = new Message("User", "Hello", String.valueOf(LocalDateTime.now()), Status.MESSAGE);

        messageService.sendMessageToChatroom(chatroomId, message);

        verify(messagingTemplate).convertAndSend("/chatroom/" + chatroomId + "/messages", message);
    }

    @Test
    public void sendJoinMessageToChatRoomTest() {
        Clock clock2 = Clock.systemDefaultZone();
        Instant timestamp = clock2.instant();

        when(clock.instant()).thenReturn(timestamp);

        Message expected = new Message("System", "User " + username + " has joined the room.", String.valueOf(timestamp), Status.JOIN);

        messageService.sendJoinMessageToChatRoom(chatroomId, username);

        verify(messagingTemplate).convertAndSend("/chatroom/" + chatroomId + "/messages", expected);
    }


}