package com.example.springwebsocketlivechat.unit;

import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.model.Status;
import com.example.springwebsocketlivechat.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class MessageServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private Clock clock;

    @InjectMocks
    private MessageService messageService;

    String username = "sdasdsa";
    String chatroomId = "chatroom";


    @Test
    public void sendMessageToChatroomTest() {
        String chatroomId = "1";
        Message message = new Message("User", "Hello", String.valueOf(LocalDateTime.now()), Status.MESSAGE);

        messageService.sendMessageToChatroom(chatroomId, message);

        verify(messagingTemplate).convertAndSend("/chatroom/" + chatroomId + "/messages", message);
    }

    @Test
    public void sendJoinMessageToChatRoomTest() {

        messageService.sendJoinMessageToChatRoom(chatroomId, username);

        verify(messagingTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }


}