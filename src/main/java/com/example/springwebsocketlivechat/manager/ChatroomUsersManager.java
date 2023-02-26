package com.example.springwebsocketlivechat.manager;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChatroomUsersManager {
    private final Map<String, List<String>> chatRooms = new HashMap<>();

    public synchronized void addUserToChatroom(
            String chatroomId,
            String username
    ) {
        List<String> users = chatRooms.computeIfAbsent(chatroomId, k -> new ArrayList<>());
        users.add(username);
    }

    public synchronized void removeUserFromChatroom(
            String chatroomId,
            String username
    ) {
        List<String> users = chatRooms.get(chatroomId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                chatRooms.remove(chatroomId);
            }
        }
    }

    public synchronized List<String> getUsersInChatroom(String chatroomId) {

        if (chatRooms.get(chatroomId) == null) return new ArrayList<>();

        return chatRooms.get(chatroomId);
    }

    public synchronized Map<String, List<String>> getAllChatroomUsers() {
        return new HashMap<>(chatRooms);
    }
}
