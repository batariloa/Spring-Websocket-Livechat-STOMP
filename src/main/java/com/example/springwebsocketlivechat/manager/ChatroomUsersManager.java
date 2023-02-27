package com.example.springwebsocketlivechat.manager;

import com.example.springwebsocketlivechat.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatroomUsersManager {
    private final Map<String, List<User>> chatRooms = new HashMap<>();
    private final ConcurrentHashMap<String, String> sessionToUserMap = new ConcurrentHashMap<>();


    public synchronized void addUserToChatroom(
            String chatroomId,
            String sessionId,
            String username
    ) {

        List<User> users = chatRooms.computeIfAbsent(chatroomId, k -> new ArrayList<>());
        users.add(new User(username, sessionId));
        
        sessionToUserMap.put(sessionId, username);
    }

    public synchronized void removeUserFromChatroom(
            String chatroomId,
            String sessionId
    ) {
        List<User> users = chatRooms.get(chatroomId);
        if (users != null) {
            users.removeIf(u -> u.getSessionId()
                                 .equals(sessionId));
            if (users.isEmpty()) {
                chatRooms.remove(chatroomId);
            }
        }
        sessionToUserMap.remove(sessionId);
    }


    public synchronized List<User> getUsersInChatroom(String chatroomId) {

        if (chatRooms.get(chatroomId) == null) return new ArrayList<>();

        return chatRooms.get(chatroomId);
    }

    public synchronized Map<String, List<User>> getAllChatroomUsers() {
        return new HashMap<>(chatRooms);
    }

    public synchronized String getUsernameForSession(String sessionId) {
        return sessionToUserMap.get(sessionId);
    }
}
