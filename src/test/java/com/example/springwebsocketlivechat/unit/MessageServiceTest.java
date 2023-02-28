package com.example.springwebsocketlivechat.unit;

import com.example.springwebsocketlivechat.model.Message;
import com.example.springwebsocketlivechat.model.Status;
import com.example.springwebsocketlivechat.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class MessageServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private Clock clock;

    private Clock fixedClock;

    @InjectMocks
    private MessageService messageService;

    String username = "sdasdsa";
    String chatroomId = "chatroom";


    @BeforeEach
    void setUp() {

        fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock)
                                      .instant();
        doReturn(fixedClock.getZone()).when(clock)
                                      .getZone();
    }

    @Test
    public void sendMessageToChatroomTest() {
        String chatroomId = "1";
        Message message = new Message("User", "Hello", clock.instant()
                                                            .atZone(clock.getZone())
                                                            .toLocalTime(), Status.MESSAGE);

        messageService.sendMessageToChatroom(chatroomId, message);

        verify(messagingTemplate).convertAndSend("/chatroom/" + chatroomId + "/messages", message);
    }

    @Test
    public void sendJoinMessageToChatRoomTest() {


        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

        messageService.sendJoinMessageToChatRoom(chatroomId, username);


        verify(messagingTemplate, times(1)).convertAndSend(anyString(), messageCaptor.capture());

        Message capturedMessage = messageCaptor.getValue();
        assertEquals(fixedClock.instant()
                               .atZone(clock.getZone())
                               .toLocalTime(), capturedMessage.getTime());

    }


}