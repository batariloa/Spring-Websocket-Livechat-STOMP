package com.example.springwebsocketlivechat.model;


import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

    public String author;
    private String body;
    private LocalTime time;
    private Status type;
}
