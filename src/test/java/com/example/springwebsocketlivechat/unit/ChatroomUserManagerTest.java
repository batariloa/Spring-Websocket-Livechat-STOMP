package com.example.springwebsocketlivechat.unit;

import com.example.springwebsocketlivechat.manager.ChatroomUsersManager;
import com.example.springwebsocketlivechat.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatroomUserManagerTest {
    private ChatroomUsersManager manager;

    @BeforeEach
    void setUp() {
        manager = new ChatroomUsersManager();
    }


    @Test
    void testAddUserToChatRoom() {

        String chatroom = "chatroom1";
        User user = new User("user123", "dada");

        Assertions.assertEquals(new ArrayList<>(), manager.getUsersInChatroom(chatroom));

        manager.addUserToChatroom(chatroom, user.getSessionId(), user.getUsername());

        Assertions.assertEquals(List.of(user), manager.getUsersInChatroom(chatroom));

    }

    @Test
    void testRemoveUserFromChatRoom() {

        String chatroom = "chatroom1";
        String username = "user123";
        String sessonId = "sdsadas";

        User user1 = new User(username, sessonId);

        manager.addUserToChatroom(chatroom, sessonId, username);

        Assertions.assertEquals(List.of(user1), manager.getUsersInChatroom(chatroom));

        manager.removeUserFromChatroom(chatroom, sessonId);
        Assertions.assertEquals(new ArrayList<>(), manager.getUsersInChatroom(chatroom));

    }

    @Test
    void testGetUsersInChatroom() {

        String chatroom = "chatroom1";
        User user1 = new User("user1", "dsada");
        User user2 = new User("user2", "sadasd");


        manager.addUserToChatroom(chatroom, user1.getSessionId(), user1.getUsername());
        manager.addUserToChatroom(chatroom, user2.getSessionId(), user2.getUsername());

        List<User> expectedUsers = Arrays.asList(user1, user2);

        Assertions.assertEquals(expectedUsers, manager.getUsersInChatroom(chatroom));
    }
}
