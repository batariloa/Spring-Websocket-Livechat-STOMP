package com.example.springwebsocketlivechat.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

    public String author;
    private String body;
    private String time;
    private Status type;
}
