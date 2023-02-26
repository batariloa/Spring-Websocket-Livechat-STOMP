package com.example.springwebsocketlivechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinChatroomMessage {
    private String roomId;
    private String username;
}
